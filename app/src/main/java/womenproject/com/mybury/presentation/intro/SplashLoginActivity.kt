package womenproject.com.mybury.presentation.intro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import android.os.Handler
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import womenproject.com.mybury.databinding.SplashWithLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import womenproject.com.mybury.data.Preference.Companion.getMyBuryLoginComplete
import womenproject.com.mybury.data.Preference.Companion.getAccountEmail
import womenproject.com.mybury.data.Preference.Companion.setAccountEmail


class SplashLoginActivity : AppCompatActivity() {

    lateinit var binding: SplashWithLoginBinding
    private val RC_SIGN_IN = 100
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.setContentView(this, R.layout.splash_with_login)
        binding.loginLayout.setOnClickListener {
            if(getAccountEmail(this).isNotEmpty()) {
                signOut()
            } else {
                signIn()
            }

        }

        val hd = Handler()
        hd.postDelayed(splashhandler(), 1000)
    }

    override fun onResume() {
        super.onResume()
        if(getMyBuryLoginComplete(this)) {
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

    private fun goToCreateAccount(account : GoogleSignInAccount) {
        Log.e("ayhan", "${account.email}, ${account.displayName}, ${account.familyName}, ${account.givenName}")
        setAccountEmail(this, account.email.toString())
        val intent = Intent(context, CreateAccountActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        finish()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        auth.signOut()

        googleSignInClient.signOut().addOnCompleteListener(this) {
            Log.e("ayhan", "login 초기화 완료")
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
                    goToCreateAccount(account)
                }
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.e("ayhan", "$e")
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.e("ayhan", "firebaseAuthWithGoogle:" + acct.id!!)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("ayhan", "signInWithCredential:success")
                        val user = auth.currentUser
                    } else {
                        Log.e("ayhan", "signInWithCredential:failure", task.exception)
                    }
                }
    }
}