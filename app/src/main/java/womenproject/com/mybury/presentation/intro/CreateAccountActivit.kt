package womenproject.com.mybury.presentation.intro

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.Preference.Companion.getAccountEmail
import womenproject.com.mybury.data.Preference.Companion.getMyBuryLoginComplete
import womenproject.com.mybury.data.Preference.Companion.getUserId
import womenproject.com.mybury.data.Preference.Companion.setMyBuryLoginComplete
import womenproject.com.mybury.data.SignUpCheckRequest
import womenproject.com.mybury.data.UseUserIdRequest
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.databinding.ActivityCreateAccountBinding
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.dialog.CanNotGoMainDialog
import womenproject.com.mybury.presentation.dialog.NetworkFailDialog
import womenproject.com.mybury.presentation.dialog.UserAlreadyExist
import womenproject.com.mybury.presentation.viewmodels.MyPageViewModel
import womenproject.com.mybury.presentation.write.AddContentType
import womenproject.com.mybury.presentation.write.WriteMemoImgAddDialogFragment
import java.io.File
import kotlin.random.Random


class CreateAccountActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreateAccountBinding
    private var file: File? = null
    private var useDefaulProfileImg = false

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account)
        binding.apply {
            topLayout.title = "프로필생성"
            topLayout.setBackBtnOnClickListener { onBackPressed() }
            startMyburyBtn.setOnClickListener(myBuryStartListener)
            profileEditBtn.setOnClickListener(profileImageEditClickLister)
            nicknameEditText.addTextChangedListener(addTextChangedListener())
            root.viewTreeObserver.addOnGlobalLayoutListener(setOnSoftKeyboardChangedListener())

            profileImg.setImageDrawable(resources.getDrawable(R.drawable.default_profile_my))
            useDefaulProfileImg = true
        }
    }

    private val checkBaseProfileImgUsable: () -> Boolean = {
        true
    }
    private val baseProfileUseListener: () -> Unit = {

        val num = Random.nextInt(2)
        if (num == 1) {
            binding.profileImg.setImageDrawable(resources.getDrawable(R.drawable.default_profile_bury))
        } else {
            binding.profileImg.setImageDrawable(resources.getDrawable(R.drawable.default_profile_my))
        }
        useDefaulProfileImg = true
        binding.executePendingBindings()
    }

    private val checkAddImgAbleListener: () -> Boolean = {
        true
    }

    private val imgAddListener: (File, Uri) -> Unit = { file: File, uri: Uri ->
        this.file = file
        useDefaulProfileImg = false
        Glide.with(this).load(uri).centerCrop().into(binding.profileImg)
    }

    private val profileImageEditClickLister = View.OnClickListener {
        WriteMemoImgAddDialogFragment(AddContentType.PROFILE, checkBaseProfileImgUsable, baseProfileUseListener,
                checkAddImgAbleListener, imgAddListener).show(supportFragmentManager, "tag")
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
            loadUserId(it)
        }

    }


    override fun onBackPressed() {
        CancelDialog().show(supportFragmentManager, "tag")
    }


    @SuppressLint("CheckResult")
    private fun signInAccount() {

        val myPageViewModel = MyPageViewModel()

        myPageViewModel.setProfileData(object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                signInAccount()
            }

            override fun start() {

            }

            override fun success() {
                goToNext()
            }

            override fun fail() {
                NetworkFailDialog().show(supportFragmentManager)
            }

        }, binding.nicknameEditText.text.toString(), file, useDefaulProfileImg)
    }


    private fun goToNext() {
        setMyBuryLoginComplete(context, true)
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        finish()
    }

    @SuppressLint("CheckResult")
    private fun getLoginToken() {
        val getTokenRequest = UseUserIdRequest(getUserId(this))
        apiInterface.getLoginToken(getTokenRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.retcode != "200") {
                        CanNotGoMainDialog().show(supportFragmentManager, "tag")
                    } else {
                        Preference.setAccessToken(this, response.accessToken)
                        Preference.setRefreshToken(this, response.refreshToken)
                        signInAccount()
                    }

                }) {
                    Log.e("myBury", "getLoginToken Fail : $it")
                    CanNotGoMainDialog().show(supportFragmentManager, "tag")
                }
    }

    @SuppressLint("CheckResult")
    private fun loadUserId(email: String) {
        val emailDataClass = SignUpCheckRequest(email)

        apiInterface.postSignUp(emailDataClass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    when (response.retcode) {
                        "200" -> {
                            Preference.setUserId(this, response.userId)
                            getLoginToken()
                        }
                        "401" -> {
                            try{
                                UserAlreadyExist().show(supportFragmentManager, "tag")
                            }catch (e :Exception){
                                Log.e("myBury","UserAlreadyExist dialog error: $e")
                            }

                        }
                        else -> {
                            CanNotGoMainDialog().show(supportFragmentManager, "tag")
                        }
                    }
                }) {
                    Log.e("myBury", "PostSignUpResponse Fail: $it")
                    try {
                        CanNotGoMainDialog().show(supportFragmentManager, "tag")
                    } catch (e:Exception){
                        Log.e("myBury","CanNotGoMainDialog error: $e")
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


