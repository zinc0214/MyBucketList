package womenproject.com.mybury.presentation.intro

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.Preference.Companion.getAccountEmail
import womenproject.com.mybury.data.Preference.Companion.getMyBuryLoginComplete
import womenproject.com.mybury.data.Preference.Companion.setMyBuryLoginComplete
import womenproject.com.mybury.data.Preference.Companion.setUserId
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.databinding.ActivityCreateAccountBinding
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.base.BaseActiviy
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.dialog.CanNotGoMainDialog
import womenproject.com.mybury.presentation.dialog.UserAlreadyExist
import womenproject.com.mybury.presentation.viewmodels.LoginViewModel
import womenproject.com.mybury.presentation.viewmodels.MyPageViewModel
import womenproject.com.mybury.presentation.viewmodels.SignUpResult
import womenproject.com.mybury.presentation.write.AddContentType
import womenproject.com.mybury.presentation.write.WriteMemoImgAddDialogFragment
import womenproject.com.mybury.util.observeNonNull
import java.io.File
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class CreateAccountActivity @Inject constructor() : BaseActiviy() {

    lateinit var binding: ActivityCreateAccountBinding
    private var file: File? = null
    private var useDefaultProfileImg = false

    private val viewModel by viewModels<LoginViewModel>()
    private val myPageViewModel by viewModels<MyPageViewModel>()

    private var userId: String = ""
    private var token: String = ""

    override fun onResume() {
        super.onResume()

        if (getMyBuryLoginComplete(context)) {
            val intent = Intent(context, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setUpViews()
        setUpObservers()
    }

    private fun setUpViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account)
        val animation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        binding.loadingLayout.loadingImg.animation = animation
        binding.apply {
            topLayout.title = "프로필생성"
            topLayout.setBackBtnOnClickListener { onBackPressed() }
            startMyburyBtn.setOnClickListener(myBuryStartListener)
            profileEditBtn.setOnClickListener(profileImageEditClickLister)
            nicknameEditText.addTextChangedListener(addTextChangedListener())
            root.viewTreeObserver.addOnGlobalLayoutListener(setOnSoftKeyboardChangedListener())

            profileImg.setImageResource(R.drawable.default_profile_my)
            useDefaultProfileImg = true
        }
    }

    private fun setUpObservers() {
        viewModel.signUpResult.observe(this) {
            when (it.first) {
                SignUpResult.Success -> {
                    userId = it.second
                    setUserId(context, userId)
                    viewModel.getLoginToken(userId)
                }
                SignUpResult.EmailExisted -> {
                    UserAlreadyExist().show(supportFragmentManager, "tag")
                }
                SignUpResult.Fail -> {
                    CanNotGoMainDialog().show(supportFragmentManager, "tag")
                }
            }
        }
        viewModel.loadLoginTokenResult.observeNonNull(this) {
            when (it.first) {
                LoadState.SUCCESS -> {
                    stopLoading()
                    it.second?.let { token ->
                        this.token = token.accessToken
                        Preference.setAccessToken(
                            MyBuryApplication.getAppContext(),
                            token.accessToken
                        )
                        Preference.setRefreshToken(
                            MyBuryApplication.getAppContext(),
                            token.refreshToken
                        )
                        createProfile()
                    }
                }
                LoadState.START -> {
                    startLoading()
                }
                LoadState.RESTART -> {
                    stopLoading()
                    viewModel.getLoginToken(userId)
                }
                LoadState.FAIL -> {
                    stopLoading()
                    CanNotGoMainDialog().show(supportFragmentManager, "tag")
                }
            }
        }
        myPageViewModel.updateProfileEvent.observeNonNull(this) {
            when (it) {
                LoadState.START -> {
                    startLoading()
                }
                LoadState.RESTART -> {
                    stopLoading()
                    createProfile()
                }
                LoadState.SUCCESS -> {
                    stopLoading()
                    goToHome()
                }
                LoadState.FAIL -> {
                    stopLoading()
                    CreateProfileFail().show(supportFragmentManager)
                }
            }
        }
    }

    private val checkBaseProfileImgUsable: () -> Boolean = {
        true
    }

    private val baseProfileUseListener: () -> Unit = {
        val num = Random.nextInt(2)
        if (num == 1) {
            binding.profileImg.setImageResource(R.drawable.default_profile_bury)
        } else {
            binding.profileImg.setImageResource(R.drawable.default_profile_my)
        }
        useDefaultProfileImg = true
        binding.executePendingBindings()
    }

    private val checkAddImgAbleListener: () -> Boolean = {
        true
    }

    private val imgAddListener: (File, Uri) -> Unit = { file: File, uri: Uri ->
        this.file = file
        useDefaultProfileImg = false
        Glide.with(this).load(uri).centerCrop().into(binding.profileImg)
    }

    private val profileImageEditClickLister = View.OnClickListener {
        WriteMemoImgAddDialogFragment(
            AddContentType.PROFILE, checkBaseProfileImgUsable, baseProfileUseListener,
            checkAddImgAbleListener, imgAddListener
        ).show(supportFragmentManager, "tag")
    }

    private fun addTextChangedListener(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    binding.nicknameText.text = "닉네임을 입력해주세요."
                    binding.startMyburyBtn.isEnabled = false
                    binding.commentImg.setBackgroundResource(R.drawable.bury_speech_01)
                } else {
                    binding.nicknameEditText.setTextColor(resources.getColor(R.color._434343))
                    binding.nicknameText.text = "${s}님 반가워요 :D"
                    binding.startMyburyBtn.isEnabled = true
                    binding.commentImg.setBackgroundResource(R.drawable.bury_speech_02)
                }
            }
        }
    }

    private fun setOnSoftKeyboardChangedListener(): ViewTreeObserver.OnGlobalLayoutListener {
        return ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            binding.root.getWindowVisibleDisplayFrame(r)

            val heightDiff = binding.root.rootView.height - (r.bottom - r.top)
            try {
                if (heightDiff < 500) {
                    binding.nicknameEditText.clearFocus()
                    binding.startMyBuryLayout.visibility = View.VISIBLE
                } else {
                    binding.startMyBuryLayout.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private val myBuryStartListener = View.OnClickListener {
        getAccountEmail(this)?.let {
            viewModel.loadUserId(it)
        }
    }


    override fun onBackPressed() {
        CancelDialog().show(supportFragmentManager, "tag")
    }

    private fun createProfile() {
        myPageViewModel.updateProfileData(
            _token = token,
            _userId = userId,
            _nickName = binding.nicknameEditText.text.toString(),
            _useDefaultImg = useDefaultProfileImg,
            _profileImg = file
        )
    }

    fun startLoading() {
        binding.loadingLayout.layout.visibility = View.VISIBLE
    }

    fun stopLoading() {
        binding.loadingLayout.layout.visibility = View.GONE
    }


    private fun goToHome() {
        setMyBuryLoginComplete(context, true)
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        finish()
    }

    inner class CreateProfileFail : BaseNormalDialogFragment() {
        init {
            TITLE_MSG = "프로필 생성 실패"
            CONTENT_MSG = "프로필 생성에 실패했습니다.\n 다시 시도해주세요."
            CANCEL_BUTTON_VISIBLE = false
            GRADIENT_BUTTON_VISIBLE = true
            CONFIRM_TEXT = "확인"
            CANCEL_ABLE = false
        }

        override fun createOnClickConfirmListener(): View.OnClickListener {
            return View.OnClickListener {
                dismiss()
                Preference.allClear(requireContext())
            }
        }
    }

}

class CancelDialog : BaseNormalDialogFragment() {

    init {
        TITLE_MSG = "계정 생성 취소"
        CONTENT_MSG = "아직 계정이 생성되지 않았습니다. 취소할 경우 작성할 내용이 삭제됩니다."
        CANCEL_BUTTON_VISIBLE = true
        GRADIENT_BUTTON_VISIBLE = false
        CONFIRM_TEXT = "계속 작성"
        CANCEL_TEXT = "생성 취소"
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            dismiss()
        }
    }

    override fun createOnClickCancelListener(): View.OnClickListener {
        return View.OnClickListener {
            Preference.allClear(requireContext())
            dismiss()
            requireActivity().finish()
        }
    }
}


