package womenproject.com.mybury.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import java.lang.ref.WeakReference

class MyBuryWebView(activity: Activity) : WebViewClient() {
    private val mActivityRef: WeakReference<Activity>

    init {
        mActivityRef = WeakReference<Activity>(activity)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url.toString()

        if (url.startsWith("mailto:")) {
            sendEmail()
            return true
        } else {
            view?.loadUrl(url)
        }
        return true
    }

    private fun sendEmail() {
        val activity: Activity? = mActivityRef.get()
        val send = Intent(Intent.ACTION_SENDTO)
        val uriText = "mailto:" + Uri.encode("mybury.info@gmail.com") +
                "?subject=" + Uri.encode("< 마이버리 문의 >")
        val uri = Uri.parse(uriText)

        send.data = uri
        activity?.startActivity(Intent.createChooser(send, "마이버리 문의하기"))
    }
}