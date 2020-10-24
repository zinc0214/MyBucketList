package womenproject.com.mybury.presentation.intro

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.UseUserIdRequest
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.databinding.SplashWithLoginBinding
import womenproject.com.mybury.presentation.CanNotGoMainDialog
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.util.ScreenUtils.Companion.setStatusBar
import kotlin.random.Random


class SplashActivity : AppCompatActivity() {

    lateinit var binding: SplashWithLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val num = Random.nextInt(2)
        if (num == 1) {
            setContentView(R.layout.splash_white)
            setStatusBar(this, R.color._ffffff)
        } else {
            setContentView(R.layout.splash_blue)
            setStatusBar(this, R.color._4656e8)
        }

        val hd = Handler()
        hd.postDelayed(splashhandler(), 1000)
    }

    private inner class splashhandler : Runnable {
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
                    CanNotGoMainDialog().show(supportFragmentManager, "tag")
                    Log.e("myBury", "getLoginToken Fail : $it")
                }


    }
}