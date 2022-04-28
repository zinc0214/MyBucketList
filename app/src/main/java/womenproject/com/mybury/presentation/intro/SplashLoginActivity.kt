package womenproject.com.mybury.presentation.intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.Preference.Companion.getAccountEmail
import womenproject.com.mybury.data.Preference.Companion.getMyBuryLoginComplete
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.data.network.APIClient
import womenproject.com.mybury.data.network.RetrofitInterface
import womenproject.com.mybury.databinding.LayoutSplashWithLoginBinding
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.dialog.CanNotGoMainDialog
import womenproject.com.mybury.presentation.dialog.NetworkFailDialog
import womenproject.com.mybury.presentation.main.WarningDialogFragment
import womenproject.com.mybury.presentation.viewmodels.CheckForLoginResult
import womenproject.com.mybury.presentation.viewmodels.LoginViewModel
import womenproject.com.mybury.util.ScreenUtils.Companion.setStatusBar
import womenproject.com.mybury.util.observeNonNull
import javax.inject.Inject

@AndroidEntryPoint
class SplashLoginActivity @Inject constructor(): AppCompatActivity() {

    lateinit var binding: LayoutSplashWithLoginBinding
    private val RC_SIGN_IN = 100
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val viewModel by viewModels<LoginViewModel>()

    val apiInterface: RetrofitInterface = APIClient.client.create(RetrofitInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setUpGoogleAuth()
        setUpViews()
        setUpObservers()
    }

    private fun setUpGoogleAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()
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

    private fun setUpViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.layout_splash_with_login)
        binding.loginLayout.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            if(!getAccountEmail(this).isNullOrEmpty() && getMyBuryLoginComplete(this)) {
                signIn()
            } else {
                signOut()
            }
        }

        val hd = Handler()
        hd.postDelayed(splashHandler(), 1000)
        setStatusBar(this, R.color._4656e8)
    }

    private fun setUpObservers() {
        viewModel.checkForLoginResult.observe(this) {
            when (it) {
                CheckForLoginResult.CheckFail -> {
                    NetworkFailDialog().show(this.supportFragmentManager)
                }
                is CheckForLoginResult.CreateAccount -> {
                    Preference.setAccountEmail(this, it.userEmail)
                    goToCreateAccountActivity() // 첫 시도이면 값을 받아서 로그인 화면으로 간다
                }
                is CheckForLoginResult.HasAccount -> {
                    Preference.setUserId(this, it.userId)
                    Preference.setMyBuryLoginComplete(this, it.isLoginComplete)
                    Preference.setAccountEmail(this, it.userEmail)
                    initToken(it.userId)
                }
            }
        }
        viewModel.loadLoginTokenResult.observeNonNull(this) {
            when(it.first) {
                LoadState.SUCCESS -> {
                    it.second?.let { token ->
                        Preference.setAccessToken(
                            MyBuryApplication.getAppContext(),
                            token.accessToken
                        )
                        Preference.setRefreshToken(
                            MyBuryApplication.getAppContext(),
                            token.refreshToken
                        )
                    }
                    goToMainActivity()// 이미 로그인 된 적이 있다. user_id 를 받아온다
                }
                LoadState.RESTART -> {
                CanNotGoMainDialog().show(supportFragmentManager, "tag")
            }
                LoadState.FAIL -> {
                    WarningDialogFragment { finish() }.show(supportFragmentManager, "tag")
                }
                else -> {
                    // do nothing
                }
            }
        }
    }

    private inner class splashHandler : Runnable {
        override fun run() {
            binding.motionLayout.transitionToEnd()
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

    private fun initToken(userId : String) {
        viewModel.getLoginToken(userId)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        auth.signOut()
        Preference.allClear(context)
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
                    viewModel.checkForIsFirstLogin(account)
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