package womenproject.com.mybury.presentation.mypage.appinfo

import android.text.Html
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import womenproject.com.mybury.R
import womenproject.com.mybury.data.DataTextType
import womenproject.com.mybury.databinding.LayoutTitleViewBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


/**
 * Created by HanAYeon on 2019-08-20.
 */

class AppInfoTextFragment : BaseFragment<LayoutTitleViewBinding, AppInfoViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.layout_title_view

    override val viewModel: AppInfoViewModel
        get() = AppInfoViewModel()

    private lateinit var type: String
    private lateinit var webSettings: WebSettings
    private lateinit var webView: WebView

    override fun initDataBinding() {
        arguments?.let {
            val args = AppInfoTextFragmentArgs.fromBundle(it)
            val textType = args.type
            type = textType!!
        }

        initWebView()
        viewDataBinding.titleLayout.backBtnOnClickListener = backBtnOnClickListener()

        when (type) {
            DataTextType.eula.toString() -> loadEula()
            DataTextType.privacy.toString() -> loadPrivacyPolicy()
            DataTextType.openSource.toString() -> loadOpenSourceText()
        }
    }

    private fun initWebView() {
        webView = viewDataBinding.webView
        webView.webViewClient = WebViewClient()
        webSettings = viewDataBinding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setSupportMultipleWindows(true)
        webSettings.useWideViewPort = true
        webSettings.setSupportZoom(true)
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
    }

    private fun loadEula() {
        viewDataBinding.titleLayout.title = "이용약관"
        viewDataBinding.textScrollView.visibility = View.GONE
        viewDataBinding.webView.loadUrl("https://www.my-bury.com/terms_of_use")
    }

    private fun loadPrivacyPolicy() {
        viewDataBinding.titleLayout.title = "개인정보 처리방침"
        viewDataBinding.textScrollView.visibility = View.GONE
        viewDataBinding.webView.loadUrl("https://www.my-bury.com/privacy_policy")
    }

    private fun loadOpenSourceText() {
        viewDataBinding.titleLayout.title = "오픈 소스 라이선스"
        viewDataBinding.webView.visibility = View.GONE
        readTextFile()
    }

    @Throws(IOException::class)
    private fun readTextFile() {
        var string: String? = ""
        val stringBuilder = StringBuilder()
        val `is`: InputStream = this.resources.openRawResource(R.raw.opensource)
        val reader = BufferedReader(InputStreamReader(`is`))
        while (true) {
            try {
                if (reader.readLine().also { string = it } == null) break
            } catch (e: IOException) {
                e.printStackTrace()
            }
            stringBuilder.append(string).append("\n")
            viewDataBinding.textView.text = Html.fromHtml(stringBuilder.toString())
        }
        `is`.close()
    }
}