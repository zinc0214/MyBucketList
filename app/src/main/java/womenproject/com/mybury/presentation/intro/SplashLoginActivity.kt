package womenproject.com.mybury.presentation.intro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import android.os.Handler
import androidx.databinding.DataBindingUtil
import womenproject.com.mybury.databinding.SplashWithLoginBinding


class SplashLoginActivity : AppCompatActivity() {

    lateinit var binding: SplashWithLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<SplashWithLoginBinding>(this, R.layout.splash_with_login)
        binding.loginLayout.setOnClickListener { l -> goGO() }

        val hd = Handler()
        hd.postDelayed(splashhandler(), 1000)
    }

    private inner class splashhandler : Runnable {
        override fun run() {
            binding.motionLayout.transitionToEnd()
        }
    }

    private fun goGO() {
        val intent = Intent(context, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        finish()
    }

}