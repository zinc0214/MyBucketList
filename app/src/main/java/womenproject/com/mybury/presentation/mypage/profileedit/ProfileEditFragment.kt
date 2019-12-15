package womenproject.com.mybury.presentation.mypage.profileedit

import android.graphics.Rect
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import womenproject.com.mybury.R
import womenproject.com.mybury.data.DefaulProfileImg
import womenproject.com.mybury.data.MyPageInfo
import womenproject.com.mybury.databinding.FragmentProfileEditBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.viewmodels.MyPageViewModel
import womenproject.com.mybury.presentation.write.AddContentType
import womenproject.com.mybury.presentation.write.WriteMemoImgAddDialogFragment
import java.io.File
import kotlin.random.Random

class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding, MyPageViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_profile_edit

    override val viewModel: MyPageViewModel
        get() = MyPageViewModel()

    private var defaultImg = "my"
    private var imgUrl: File? = null
    private var lastNickname = ""
    private var lastImg = ""
    private var useDefatilImg = false

    override fun initDataBinding() {
        getMyProfileInfo()
        viewDataBinding.title = "프로필 수정"
    }

    private fun setUpView() {

        viewDataBinding.profileImageEditClickListener = profileImageEditClickLister
        viewDataBinding.backBtnOnClickListener = cancelClickListener
        viewDataBinding.saveBtnOnClickListener = saveBtnOnClickListener
        viewDataBinding.nicknameEditText.addTextChangedListener(addTextChangedListener())
        viewDataBinding.root.viewTreeObserver.addOnGlobalLayoutListener(setOnSoftKeyboardChangedListener())

    }

    private fun addTextChangedListener(): TextWatcher {

        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setSaveBtnEnabled()
            }

        }
    }

    private val checkBaseProfileImgUsable: () -> Boolean = {
        true
    }

    private fun seyMyProfileImg(imgUrl: String?) {
        if(imgUrl.isNullOrBlank()) {
            setDefaultImg()
        } else {
            Glide.with(this).load(imgUrl).into(viewDataBinding.profileImg)
        }


    }


    private fun getMyProfileInfo() {

        viewModel.getMyPageData(object : BaseViewModel.MoreCallBackAny {
            override fun restart() {

            }

            override fun start() {
                startLoading()
            }

            override fun success(myPageInfo: Any) {
                val info = myPageInfo as MyPageInfo
                Log.e("ayhan", "gogu221ng???")
                stopLoading()

                viewDataBinding.nicknameEditText.setText(info.name)
                lastNickname = info.name
                lastImg = info.imageUrl.toString()
                setUpView()
                seyMyProfileImg(info.imageUrl)
            }

            override fun fail() {
                stopLoading()
            }

        })

    }
    private fun setMyProfileInfo() {

        viewModel.setProfileData(object :BaseViewModel.Simple3CallBack {
            override fun restart() {
                setMyProfileInfo()
            }

            override fun start() {
                startLoading()
            }

            override fun success() {
                Toast.makeText(context!!, "프로필이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                stopLoading()

                lastNickname = viewDataBinding.nicknameEditText.text.toString()
                defaultImg = lastImg

                setSaveBtnEnabled()
            }

            override fun fail() {
                stopLoading()
            }

        }, viewDataBinding.nicknameEditText.text.toString(), imgUrl, useDefatilImg)
    }

    private fun setSaveBtnEnabled() {

        Log.e("ayhan", "de: $defaultImg, last: $lastImg")

        if (lastNickname != viewDataBinding.nicknameEditText.text.toString() || lastImg != defaultImg) {
            viewDataBinding.nicknameEditText.setTextColor(resources.getColor(R.color._434343))
            viewDataBinding.profileSave.isEnabled = true
        } else {
            viewDataBinding.nicknameEditText.setTextColor(resources.getColor(R.color._888888))
            viewDataBinding.profileSave.isEnabled = false
        }


    }

    private val baseProfileUseListener: () -> Unit = {

        setDefaultImg()
        setSaveBtnEnabled()
        imgUrl = null

    }

    private fun setDefaultImg() {
        val num = Random.nextInt(2)
        Log.e("ayhan", "nume: $num")
        defaultImg = if (num == 1) {
            viewDataBinding.profileImg.setImageDrawable(resources.getDrawable(R.drawable.default_profile_bury))
            DefaulProfileImg().bury
        } else {
            viewDataBinding.profileImg.setImageDrawable(resources.getDrawable(R.drawable.default_profile_my))
            DefaulProfileImg().my
        }
        useDefatilImg = true
        viewDataBinding.executePendingBindings()
    }


    private val checkAddImgAbleListener: () -> Boolean = {
        true
    }

    private val imgAddListener: (File, Uri) -> Unit = { file: File, uri: Uri ->
        Glide.with(context!!).load(uri).centerCrop().into(viewDataBinding.profileImg)
        defaultImg = file.toString()
        imgUrl = file
        useDefatilImg = false
        setSaveBtnEnabled()
    }

    private val profileImageEditClickLister = View.OnClickListener {
        WriteMemoImgAddDialogFragment(AddContentType.PROFILE, checkBaseProfileImgUsable, baseProfileUseListener,
                checkAddImgAbleListener, imgAddListener).show(activity!!.supportFragmentManager, "tag")

    }

    private val cancelClickListener = View.OnClickListener {
        if (lastNickname != viewDataBinding.nicknameEditText.text.toString() || lastImg != defaultImg) {
            CancelDialog().show(activity!!.supportFragmentManager, "tag")
        } else {
            activity!!.onBackPressed()
        }
    }

    private val saveBtnOnClickListener = View.OnClickListener {
        setMyProfileInfo()
    }


    private fun setOnSoftKeyboardChangedListener(): ViewTreeObserver.OnGlobalLayoutListener {
        return ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            viewDataBinding.root.getWindowVisibleDisplayFrame(r)

            val heightDiff = viewDataBinding.root.rootView.height - (r.bottom - r.top)
            try {
                if (heightDiff < 500) {
                    viewDataBinding.nicknameEditText.clearFocus()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    class CancelDialog : BaseNormalDialogFragment() {

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
            }
        }

        override fun createOnClickConfirmListener(): View.OnClickListener {
            return View.OnClickListener {
                dismiss()
                activity!!.onBackPressed()
            }
        }

    }


    private fun <T> LiveData<T>.observe(block: (T) -> Unit) = observe(this@ProfileEditFragment, Observer { block(it) })


}