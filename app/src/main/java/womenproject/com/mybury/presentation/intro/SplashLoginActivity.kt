package womenproject.com.mybury.presentation.intro

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.Preference.Companion.getAccountEmail
import womenproject.com.mybury.data.Preference.Companion.getMyBuryLoginComplete
import womenproject.com.mybury.data.Preference.Companion.getUserId
import womenproject.com.mybury.data.Preference.Companion.setAccountEmail
import womenproject.com.mybury.data.Preference.Companion.setMyBuryLoginComplete
import womenproject.com.mybury.data.Preference.Companion.setUserId
import womenproject.com.mybury.data.SignUpCheckRequest
import womenproject.com.mybury.data.UseUserIdRequest
import womenproject.com.mybury.data.network.APIClient
import womenproject.com.mybury.data.network.RetrofitInterface
import womenproject.com.mybury.databinding.LayoutSplashWithLoginBinding
import womenproject.com.mybury.presentation.CanNotGoMainDialog
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.NetworkFailDialog
import womenproject.com.mybury.presentation.main.WarningDialogFragment
import womenproject.com.mybury.util.ScreenUtils.Companion.setStatusBar


class SplashLoginActivity : AppCompatActivity() {

    lateinit var binding: LayoutSplashWithLoginBinding
    private val RC_SIGN_IN = 100
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    val apiInterface = APIClient.client.create(RetrofitInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.setContentView(this, R.layout.layout_splash_with_login)
        binding.loginLayout.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            if (getAccountEmail(this)?.isNotEmpty()!! || !getMyBuryLoginComplete(this)) {
                signOut()
            } else {
                signIn()
            }
        }

        val hd = Handler()
        hd.postDelayed(splashhandler(), 1000)
        setStatusBar(this, R.color._4656e8)
    }

    override fun onResume() {
        super.onResume()
        if (getMyBuryLoginComplete(this)) {
            val intent = Intent(context, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            finish()
        }
    }

    private inner class splashhandler : Runnable {
        override fun run() {
            binding.motionLayout.transitionToEnd()
        }
    }

    @SuppressLint("CheckResult")
    private fun checkForIsFirstLogin(account: GoogleSignInAccount) {

        val emailDataClass = SignUpCheckRequest(account.email.toString())
        apiInterface.postSignUpCheck(emailDataClass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    setAccountEmail(this, emailDataClass.email)
                    if (response.signUp) {
                        setUserId(this, response.userId)
                        setMyBuryLoginComplete(this, true)
                        initToken()
                    } else {
                        goToCreateAccountActivity() // 첫 시도이면 값을 받아서 로그인 화면으로 간다
                    }
                }) {
                    val networkFailDialog = NetworkFailDialog()
                    networkFailDialog.show(this.supportFragmentManager)
                    Log.e("myBury", "getCheckForIsFirstLogin Fail $it")
                }

    }

    private fun goToCreateAccountActivity() {
        val intent = Intent(context, CreateAccountActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        finish()
    }

    private fun goToMainActivity() {
        Preference.setMyBuryLoginComplete(context, true)
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        finish()
    }

    @SuppressLint("CheckResult")
    private fun initToken() {

        val getTokenRequest = UseUserIdRequest(getUserId(this))

        apiInterface.getLoginToken(getTokenRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.retcode == "200") {
                        Preference.setAccessToken(this, response.accessToken)
                        Preference.setRefreshToken(this, response.refreshToken)
                        goToMainActivity()// 이미 로그인 된 적이 있다. user_id 를 받아온다
                    } else {
                        CanNotGoMainDialog().show(supportFragmentManager, "tag")
                    }
                }) {
                    WarningDialogFragment {
                        finish()
                    }.show(supportFragmentManager, "tag")
                    Log.e("myBury", "getLoginToken Fail : $it")
                }


    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        auth.signOut()

        googleSignInClient.signOut().addOnCompleteListener(this) {
            //로그인 초기화 완료
            signIn()
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    checkForIsFirstLogin(account)
                }
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                binding.progressBar.visibility = View.GONE
                CanNotGoMainDialog().show(supportFragmentManager, "tag")
                Log.e("myBury", "GoogleLoginFail : $e")
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        CanNotGoMainDialog().show(supportFragmentManager, "tag")
                        Log.e("myBury", "signInWithCredential Fail", task.exception)
                    }
                }
    }
}