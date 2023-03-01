package womenproject.com.mybury.presentation.mypage.profileedit

import android.content.Context
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.data.DefaulProfileImg
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.databinding.FragmentProfileEditBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.dialog.LoadFailDialog
import womenproject.com.mybury.presentation.viewmodels.MyPageViewModel
import womenproject.com.mybury.presentation.write.AddContentType
import womenproject.com.mybury.presentation.write.WriteMemoImgAddDialogFragment
import womenproject.com.mybury.util.observeNonNull
import womenproject.com.mybury.util.showToast
import java.io.File
import kotlin.random.Random

@AndroidEntryPoint
class ProfileEditFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileEditBinding
    private val viewModel by viewModels<MyPageViewModel>()

    private lateinit var imm: InputMethodManager
    private var isKeyboardUp = false

    private var defaultImg = "my"
    private var imgUrl: File? = null
    private var lastNickname = ""
    private var lastImg = ""
    private var useDetailImg = false

    private val cancelConfirm: (Boolean) -> Unit = {
        if (it) {
            isCancelConfirm = it
            requireActivity().onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_edit, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataBinding()
        setUpObservers()
    }

    private fun initDataBinding() {
        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        getMyProfileInfo()
        binding.title = "프로필 수정"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val goToActionCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isCancelConfirm) {
                    isEnabled = false
                    requireActivity().onBackPressed()
                } else {
                    cancelClickAction()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, goToActionCallback)
    }


    private fun setUpView() {
        binding.profileImageEditClickListener = profileImageEditClickLister
        binding.backBtnOnClickListener = cancelClickListener()
        binding.saveBtnOnClickListener = saveBtnOnClickListener
        binding.nicknameEditText.addTextChangedListener(addTextChangedListener())
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(setOnSoftKeyboardChangedListener())
    }

    private fun setUpObservers() {
        viewModel.myPageInfo.observeNonNull(viewLifecycleOwner) {
            binding.nicknameEditText.setText(it.name)
            lastNickname = it.name
            lastImg = it.imageUrl.toString()
            defaultImg = it.imageUrl.toString()
            setUpView()
            seyMyProfileImg(it.imageUrl)
        }

        viewModel.loadMyPageInfoEvent.observeNonNull(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> startLoading()
                LoadState.RESTART -> getMyProfileInfo()
                LoadState.SUCCESS -> stopLoading()
                LoadState.FAIL -> {
                    stopLoading()
                    LoadFailDialog {
                        onBackPressedFragment()
                    }.show(requireActivity().supportFragmentManager, "LoadFailDialog")
                }
            }
        }

        viewModel.updateProfileEvent.observeNonNull(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> {
                    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                    startLoading()
                }
                LoadState.RESTART -> {
                    setMyProfileInfo()
                }
                LoadState.SUCCESS -> {
                    requireContext().showToast("프로필이 수정되었습니다.")
                    lastNickname = binding.nicknameEditText.text.toString()
                    defaultImg = lastImg
                    setSaveBtnEnabled()
                    cancelClickAction()
                    stopLoading()
                }
                LoadState.FAIL -> {
                    stopLoading()
                    requireContext().showToast("프로필 수정에 실패했습니다. 다시 시도해주세요.")
                }
            }
        }
    }

    private fun addTextChangedListener(): TextWatcher {

        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.nicknameEditText.setTextColor(requireContext().getColor(R.color._434343))
                setSaveBtnEnabled()
            }

        }
    }

    private val checkBaseProfileImgUsable: () -> Boolean = {
        true
    }

    private fun seyMyProfileImg(imgUrl: String?) {
        if (imgUrl.isNullOrBlank()) {
            setDefaultImg()
        } else {
            Glide.with(this).load(imgUrl)
                .override(100, 100)
                .into(binding.profileImg)
        }
    }

    private fun getMyProfileInfo() {
        viewModel.getMyPageData()
    }

    private fun setMyProfileInfo() {
        viewModel.updateProfileData(
            _nickName = binding.nicknameEditText.text.toString(),
            _profileImg = imgUrl,
            _useDefaultImg = useDetailImg
        )
    }

    private fun setSaveBtnEnabled() {

        if (binding.nicknameEditText.text.isNotBlank() && (lastNickname != binding.nicknameEditText.text.toString() || lastImg != defaultImg)) {
            binding.nicknameEditText.setTextColor(resources.getColor(R.color._434343))
            binding.profileSave.isEnabled = true
            isCancelConfirm = false
        } else {
            binding.nicknameEditText.setTextColor(resources.getColor(R.color._888888))
            binding.profileSave.isEnabled = false
            isCancelConfirm = true
        }
    }

    private val baseProfileUseListener: () -> Unit = {

        setDefaultImg()
        setSaveBtnEnabled()
        imgUrl = null

    }

    private fun setDefaultImg() {
        val num = Random.nextInt(2)
        defaultImg = if (num == 1) {
            binding.profileImg.setImageResource(R.drawable.default_profile_bury)
            DefaulProfileImg().bury
        } else {
            binding.profileImg.setImageResource(R.drawable.default_profile_my)
            DefaulProfileImg().my
        }
        useDetailImg = true
        binding.executePendingBindings()
    }


    private val checkAddImgAbleListener: () -> Boolean = {
        true
    }

    private val imgAddListener: (File, Uri) -> Unit = { file: File, uri: Uri ->
        Glide.with(requireContext()).load(uri).centerCrop().into(binding.profileImg)
        defaultImg = file.toString()
        imgUrl = file
        useDetailImg = false
        setSaveBtnEnabled()
    }

    private val profileImageEditClickLister = View.OnClickListener {
        WriteMemoImgAddDialogFragment(
            AddContentType.PROFILE, checkBaseProfileImgUsable, baseProfileUseListener,
            checkAddImgAbleListener, imgAddListener
        ).show(requireActivity().supportFragmentManager, "tag")
    }

    private fun cancelClickListener() = View.OnClickListener {
        cancelClickAction()
    }

    private fun cancelClickAction() {
        if (lastNickname != binding.nicknameEditText.text.toString() || lastImg != defaultImg) {
            CancelDialog(cancelConfirm).show(requireActivity().supportFragmentManager, "tag")
        } else {
            if (isKeyboardUp) {
                imm.hideSoftInputFromWindow(requireView().windowToken, 0)
            }
            cancelConfirm.invoke(true)
        }
    }

    private val saveBtnOnClickListener = View.OnClickListener {
        setMyProfileInfo()
    }

    private fun setOnSoftKeyboardChangedListener(): ViewTreeObserver.OnGlobalLayoutListener {
        return ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            binding.root.getWindowVisibleDisplayFrame(r)

            val heightDiff = binding.root.rootView.height - (r.bottom - r.top)
            try {
                if (heightDiff < 500) {
                    binding.nicknameEditText.clearFocus()
                    binding.nicknameEditText.setTextColor(
                        getColor(
                            requireContext(),
                            R.color._888888
                        )
                    )
                    binding.badgeLayout.visibility = View.VISIBLE
                    isKeyboardUp = false
                } else {
                    binding.badgeLayout.visibility = View.GONE
                    isKeyboardUp = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    class CancelDialog(val confirm: (Boolean) -> Unit) : BaseNormalDialogFragment() {

        init {
            TITLE_MSG = "프로필 수정"
            CONTENT_MSG = "수정한 내용을 삭제하고 이전 프로필을 유지합니다."
            CANCEL_BUTTON_VISIBLE = true
            GRADIENT_BUTTON_VISIBLE = false
            CONFIRM_TEXT = "이전 프로필 유지"
            CANCEL_TEXT = "취소"
        }

        override fun createOnClickCancelListener(): View.OnClickListener {
            return View.OnClickListener {
                dismiss()
                confirm.invoke(false)
            }
        }

        override fun createOnClickConfirmListener(): View.OnClickListener {
            return View.OnClickListener {
                dismiss()
                confirm.invoke(true)
            }
        }
    }

    private fun <T> LiveData<T>.observe(block: (T) -> Unit) =
        observe(this@ProfileEditFragment, { block(it) })

}