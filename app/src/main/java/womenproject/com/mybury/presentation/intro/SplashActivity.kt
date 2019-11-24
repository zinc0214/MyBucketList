package womenproject.com.mybury.presentation.intro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import kotlin.random.Random
import android.os.Handler
import android.util.Log
import womenproject.com.mybury.databinding.SplashWithLoginBinding
import womenproject.com.mybury.presentation.MainActivity


class SplashActivity : AppCompatActivity() {

    lateinit var binding : SplashWithLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val num = Random.nextInt(2)
        Log.e("ayhan", "nume: $num")
        if (num == 1) {
            setContentView(R.layout.splash_white)
        } else {
            setContentView(R.layout.splash_blue)
        }

        val hd = Handler()
        hd.postDelayed(splashhandler(), 1000)
    }

    private inner class splashhandler : Runnable {
        override fun run() {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            finish()
        }
    }
}