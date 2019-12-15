package womenproject.com.mybury.presentation.mypage.logininfo

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
import womenproject.com.mybury.data.Preference.Companion.setMyBuryLoginComplete
import womenproject.com.mybury.data.UseUserIdRequest
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.databinding.FragmentLoginInfoBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment

/**
 * Created by HanAYeon on 2019-09-16.
 */

class LoginInfoFragment : BaseFragment<FragmentLoginInfoBinding, LoginInfoViewModel>() {


    override val layoutResourceId: Int
        get() = R.layout.fragment_login_info

    override val viewModel: LoginInfoViewModel
        get() = LoginInfoViewModel()


    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun initDataBinding() {
        viewDataBinding.backLayout.title = "로그인 정보"
        viewDataBinding.backLayout.backBtnOnClickListener = setOnBackBtnClickListener()
        viewDataBinding.activity = this
    }

    fun accountDeleteClickListener() {
        signOutMyBury()
    }

    fun googleLogoutClickListener() {
        initGoogle()
        signOut()
    }


    @SuppressLint("CheckResult")
    private fun signOutMyBury() {
        val userIdRequest = UseUserIdRequest(Preference.getUserId(context!!))

        apiInterface.postSignOut(getAccessToken(context!!), userIdRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.retcode != "200") {
                        LogoutOrSignOutFailed("계정삭제 실패").show(activity!!.supportFragmentManager, "tag")
                    } else {
                        setMyBuryLoginComplete(context!!, false)
                        AccountDeleteDialogFragment().show(activity!!.supportFragmentManager, "tag")
                    }

                }) {
                    Log.e("ayhan", it.toString())
                    LogoutOrSignOutFailed("계정삭제 실패").show(activity!!.supportFragmentManager, "tag")
                }

    }

    private fun initGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(activity!!, gso)
        auth = FirebaseAuth.getInstance()

    }

    private fun signOut() {
        auth.signOut()

        googleSignInClient.signOut().addOnCompleteListener(activity!!) {
            Log.e("ayhan", "로그아웃 완료?")
            setMyBuryLoginComplete(context!!, false)
            LogoutSuccess().show(activity!!.supportFragmentManager)
        }
    }

}


class LogoutSuccess : BaseNormalDialogFragment() {

    init {
        TITLE_MSG = "로그아웃 완료"
        CONTENT_MSG = "로그아웃이 완료되었습니다."
        CANCEL_BUTTON_VISIBLE = false
        GRADIENT_BUTTON_VISIBLE = true
        CONFIRM_TEXT = "확인"
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            dismiss()
            activity!!.finish()
        }
    }

}


class LogoutOrSignOutFailed(title: String) : BaseNormalDialogFragment() {

    init {
        TITLE_MSG = title
        CONTENT_MSG = "실패했습니다..."
        CANCEL_BUTTON_VISIBLE = false
        GRADIENT_BUTTON_VISIBLE = true
        CONFIRM_TEXT = "확인"
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            dismiss()
            activity!!.finish()
        }
    }

}
