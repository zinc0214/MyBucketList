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
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference.Companion.getMyBuryLoginComplete
import womenproject.com.mybury.data.Preference.Companion.getAccountEmail
import womenproject.com.mybury.data.Preference.Companion.setMyBuryLoginCompelete
import womenproject.com.mybury.data.network.bucketListApi
import womenproject.com.mybury.databinding.ActivityCreateAccountBinding
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.base.BaseActiviy
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.write.AddContentType
import womenproject.com.mybury.presentation.write.WriteMemoImgAddDialogFragment
import java.io.File
import kotlin.random.Random
import womenproject.com.mybury.data.network.APIClient
import womenproject.com.mybury.data.network.RetrofitInterface


class CreateAccountActivity : BaseActiviy() {

    lateinit var binding: ActivityCreateAccountBinding

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account)
        binding.topLayout.title = "프로필생성"
        binding.startMyburyBtn.setOnClickListener(myBuryStartListener)
        binding.profileEditBtn.setOnClickListener(profileImageEditClickLister)
        binding.nicknameEditText.addTextChangedListener(addTextChangedListener())
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(setOnSoftKeyboardChangedListener())

    }


    private val checkBaseProfileImgUsable: () -> Boolean = {
        true
    }
    private val baseProfileUseListener: () -> Unit = {

        val num = Random.nextInt(2)
        Log.e("ayhan", "nume: $num")
        if (num == 1) {
            binding.profileImg.setImageDrawable(resources.getDrawable(R.drawable.default_profile_bury))
        } else {
            binding.profileImg.setImageDrawable(resources.getDrawable(R.drawable.default_profile_my))
        }
        binding.executePendingBindings()
    }

    private val checkAddImgAbleListener: () -> Boolean = {
        true
    }

    private val imgAddListener: (File, Uri) -> Unit = { file: File, uri: Uri ->
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
        val email = getAccountEmail(context)


        val apiInterface = APIClient.client.create(RetrofitInterface::class.java)

        apiInterface.postSignIn("ayhan@rsupport.com")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    goToNext()
                }
                .subscribe({ response ->
                    Log.e("ayhan_2", response.string())
                    goToNext()
                }) {
                    Log.e("ayhan_3", it.toString())
                    goToNext()

                }
    }

    private fun goToNext() {
        setMyBuryLoginCompelete(context, true)
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        finish()
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


