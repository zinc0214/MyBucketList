package womenproject.com.mybury.presentation.intro

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.UseUserIdRequest
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.databinding.LayoutSplashWithLoginBinding
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.dialog.CanNotGoMainDialog
import womenproject.com.mybury.presentation.dialog.NetworkFailDialog
import womenproject.com.mybury.presentation.main.WarningDialogFragment
import womenproject.com.mybury.util.ScreenUtils.Companion.setStatusBar
import womenproject.com.mybury.util.isConnectionOn
import kotlin.random.Random

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    lateinit var binding: LayoutSplashWithLoginBinding
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
            hd.postDelayed(Splashhandler(), 1000)
        }
    }

    private inner class Splashhandler : Runnable {
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

    @SuppressLint("CheckResult")
    private fun initToken() {

        val getTokenRequest = UseUserIdRequest(Preference.getUserId(this))

        apiInterface.getLoginToken(getTokenRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.retcode == "200") {
                        Preference.setAccessToken(this, response.accessToken)
                        Preference.setRefreshToken(this, response.refreshToken)
                        goToNext()
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
}