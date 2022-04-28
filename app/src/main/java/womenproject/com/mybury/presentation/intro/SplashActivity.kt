package womenproject.com.mybury.presentation.intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.databinding.LayoutSplashWithLoginBinding
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.dialog.CanNotGoMainDialog
import womenproject.com.mybury.presentation.dialog.NetworkFailDialog
import womenproject.com.mybury.presentation.main.WarningDialogFragment
import womenproject.com.mybury.presentation.viewmodels.LoginViewModel
import womenproject.com.mybury.util.ScreenUtils.Companion.setStatusBar
import womenproject.com.mybury.util.isConnectionOn
import womenproject.com.mybury.util.observeNonNull
import kotlin.random.Random

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    lateinit var binding: LayoutSplashWithLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val num = Random.nextInt(2)
        if (num == 1) {
            setContentView(R.layout.layout_splash_white)
            setStatusBar(this, R.color._ffffff)
        } else {
            setContentView(R.layout.layout_splash_blue)
            setStatusBar(this, R.color._4656e8)
        }

        if (!this.isConnectionOn()) {
            NetworkFailDialog().show(supportFragmentManager, "tag")
        } else {
            val hd = Handler()
            hd.postDelayed(SplashHandler(), 1000)
        }

        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.loadLoginTokenResult.observeNonNull(this) {
            when (it.first) {
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
                    goToNext()
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

    private inner class SplashHandler : Runnable {
        override fun run() {
            initToken()
        }
    }

    private fun goToNext() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        finish()
    }

    private fun initToken() {
        val userId = Preference.getUserId(this)
        userId?.let {
            viewModel.getLoginToken(it)
        }
    }
}