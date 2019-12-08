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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.data.GetTokenRequest
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
import womenproject.com.mybury.data.Preference.Companion.getMyBuryLoginComplete
import womenproject.com.mybury.data.Preference.Companion.getUserId
import womenproject.com.mybury.data.Preference.Companion.setMyBuryLoginCompelete
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.databinding.ActivityCreateAccountBinding
import womenproject.com.mybury.presentation.CanNotGoMainDialog
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.base.BaseActiviy
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.write.AddContentType
import womenproject.com.mybury.presentation.write.WriteMemoImgAddDialogFragment
import java.io.File
import kotlin.random.Random
import okhttp3.RequestBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import womenproject.com.mybury.data.DefaulProfileImg
import womenproject.com.mybury.util.Converter.Companion.getUriToDrawable
import womenproject.com.mybury.util.fileToMultipartFile
import womenproject.com.mybury.util.stringToMultipartFile


class CreateAccountActivity : BaseActiviy() {

    lateinit var binding: ActivityCreateAccountBinding
    private var defaultImg = ""
    private var file : File ?= null

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


        getLoginToken()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account)
        binding.apply {
            topLayout.title = "프로필생성"
            startMyburyBtn.setOnClickListener(myBuryStartListener)
            profileEditBtn.setOnClickListener(profileImageEditClickLister)
            nicknameEditText.addTextChangedListener(addTextChangedListener())
            root.viewTreeObserver.addOnGlobalLayoutListener(setOnSoftKeyboardChangedListener())

            profileImg.setImageDrawable(resources.getDrawable(R.drawable.default_profile_my))
        }
        defaultImg = DefaulProfileImg().bury
    }

    private val checkBaseProfileImgUsable: () -> Boolean = {
        true
    }
    private val baseProfileUseListener: () -> Unit = {

        val num = Random.nextInt(2)
        Log.e("ayhan", "nume: $num")
        if (num == 1) {
            binding.profileImg.setImageDrawable(resources.getDrawable(R.drawable.default_profile_bury))
            defaultImg = DefaulProfileImg().bury
        } else {
            binding.profileImg.setImageDrawable(resources.getDrawable(R.drawable.default_profile_my))
            defaultImg = DefaulProfileImg().my
        }
        binding.executePendingBindings()
    }

    private val checkAddImgAbleListener: () -> Boolean = {
        true
    }

    private val imgAddListener: (File, Uri) -> Unit = { file: File, uri: Uri ->
        this.file = file
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
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


    private val myBuryStartListener = View.OnClickListener {
        signInAccount()
    }


    override fun onBackPressed() {
        CancelDialog().show(supportFragmentManager, "tag")
    }


    @SuppressLint("CheckResult")
    private fun signInAccount() {

        val token = getAccessToken(this)
        val userId = getUserId(this).stringToMultipartFile("userId")
        val nickName = binding.nicknameEditText.text.toString().stringToMultipartFile("nickName")
        val profileImg = if(file == null) {
            defaultImg.stringToMultipartFile("profileImg")
        } else {
            file!!.fileToMultipartFile("profileImg")
        }


        apiInterface.postCreateProfile(token, userId, nickName, profileImg)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (it.retcode == "200") {
                        goToNext()
                    } else {
                        Toast.makeText(this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }

                }
                .subscribe({ response ->
                    Log.e("ayhan", "createAccountResponse:${response.retcode}")
                }) {
                    Log.e("ayhan", "createAccountFail: $it")

                }
    }


    private fun goToNext() {
        setMyBuryLoginCompelete(context, true)
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        finish()
    }

    @SuppressLint("CheckResult")
    private fun getLoginToken() {
        val getTokenRequest = GetTokenRequest(getUserId(this))
        Log.e("ayhan", "userId : ${getTokenRequest.userId}")

        apiInterface.getLoginToken(getTokenRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Log.e("ayhan", "getTokenResponse ${response.accessToken}")
                    if (response.retcode != "200") {
                        CanNotGoMainDialog().show(supportFragmentManager, "tag")
                    } else {
                        Preference.setAccessToken(this, response.accessToken)
                    }

                }) {
                    Log.e("ayhan", "getTokenResponse $it")
                    CanNotGoMainDialog().show(supportFragmentManager, "tag")
                }


    }

}

class CancelDialog : BaseNormalDialogFragment() {

    init {
        TITLE_MSG = "계정 생성 취소"
        CONTENT_MSG = "계정 생성을 취소하면 처음부터 다시 시작해야됩니다...."
        CANCEL_BUTTON_VISIBLE = true
        GRADIENT_BUTTON_VISIBLE = false
        CONFIRM_TEXT = "확인"
        CANCEL_TEXT = "취소"
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            dismiss()
            activity!!.finish()
        }
    }

    override fun createOnClickCancelListener(): View.OnClickListener {
        return View.OnClickListener {
            dismiss()
        }
    }

}


