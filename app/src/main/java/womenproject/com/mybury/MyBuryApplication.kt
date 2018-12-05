package womenproject.com.mybury

import android.app.Application
import android.content.Context

/**
 * Created by HanAYeon on 2018. 12. 4..
 */

class MyBuryApplication : Application() {

    companion object {

        lateinit var context: Context

        fun getAppContext() : Context {
            return MyBuryApplication.context
        }
    }

    override fun onCreate() {
        super.onCreate()
        MyBuryApplication.context = applicationContext
    }
}