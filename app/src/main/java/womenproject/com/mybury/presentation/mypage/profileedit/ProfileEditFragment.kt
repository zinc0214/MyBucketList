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
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
import womenproject.com.mybury.data.Preference.Companion.getUserId
import womenproject.com.mybury.databinding.FragmentProfileEditBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
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

    override fun initDataBinding() {


        //
        //  viewDataBinding.viewModel = viewModel

        getMyProfileInfo()
        viewDataBinding.title = "프로필 수정"

/*        viewModel.apply {
            myPageInfo.observe {
                Log.e("ayhan", "gogung???")
                viewDataBinding.nicknameEditText.setText(it.name)
                lastNickname = it.name
                lastImg = it.imageUrl
                setUpView()
                seyMyProfileImg(it.imageUrl)
            }
        }*/


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
        val userId = getUserId(context!!)
        val tokenId = getAccessToken(context!!)


        viewModel.getMyPageData(object : MyPageViewModel.GetMyPageInfoListener {
            override fun start() {
                startLoading()
            }

            override fun success(myPageInfo: MyPageInfo) {
                Log.e("ayhan", "gogu221ng???")
                stopLoading()

                viewDataBinding.nicknameEditText.setText(myPageInfo.name)
                lastNickname = myPageInfo.name
                lastImg = myPageInfo.imageUrl.toString()
                setUpView()
                seyMyProfileImg(myPageInfo.imageUrl)
            }

            override fun fail() {
                stopLoading()
            }

        }, tokenId, userId)
    }

    private fun setMyProfileInfo() {
        val userId = getUserId(context!!)
        val tokenId = getAccessToken(context!!)

        viewModel.setProfileData(object : MyPageViewModel.SetMyProfileListener {
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

        }, tokenId, userId, viewDataBinding.nicknameEditText.text.toString(), imgUrl)
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
        viewDataBinding.executePendingBindings()
    }


    private val checkAddImgAbleListener: () -> Boolean = {
        true
    }

    private val imgAddListener: (File, Uri) -> Unit = { file: File, uri: Uri ->
        Glide.with(context!!).load(uri).centerCrop().into(viewDataBinding.profileImg)
        defaultImg = file.toString()
        imgUrl = file
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