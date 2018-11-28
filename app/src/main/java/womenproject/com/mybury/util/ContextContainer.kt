package womenproject.com.mybury.util

import android.content.Context
import android.content.res.Resources

/**
 * Created by HanAYeon on 2018. 11. 26..
 */

class ContextContainer private constructor() {

    private var applicationContext: Context? = null

    companion object {

        val instance: ContextContainer
            get() = Singleton.INSTANCE
    }

    val resources: Resources
        get() {
            if (applicationContext == null) {
                throw IllegalStateException("Application Context is not initialized")
            }

            return applicationContext!!.resources
        }

    fun initApplicationContext(context: Context) {
        /**
         * apk 버전일 경우 Application 클래스에서 초기화
         * aar 버전일 경우 서버 세팅 or 실행 전 초기화
         */

        applicationContext = context.applicationContext
    }

    fun getApplicationContext(): Context {
        if (applicationContext == null) {
            throw IllegalStateException("Application Context is not initialized")
        }

        return applicationContext as Context
    }

    private object Singleton {
        val INSTANCE = ContextContainer()
    }


}