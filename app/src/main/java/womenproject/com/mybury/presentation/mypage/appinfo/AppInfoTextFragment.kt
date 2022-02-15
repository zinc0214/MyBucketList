package womenproject.com.mybury.presentation.mypage.appinfo

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
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

@AndroidEntryPoint
class AppInfoTextFragment : BaseFragment() {

    private lateinit var binding : LayoutTitleViewBinding
    private lateinit var type: String
    private lateinit var webSettings: WebSettings
    private lateinit var webView: WebView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,  R.layout.layout_title_view, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initDataBinding()
    }

    private fun initDataBinding() {
        arguments?.let {
            val args = AppInfoTextFragmentArgs.fromBundle(it)
            val textType = args.type
            type = textType!!
        }

        initWebView()
        binding.titleLayout.backBtnOnClickListener = backBtnOnClickListener()

        when (type) {
            DataTextType.eula.toString() -> loadEula()
            DataTextType.privacy.toString() -> loadPrivacyPolicy()
            DataTextType.openSource.toString() -> loadOpenSourceText()
        }
    }

    private fun initWebView() {
        webView = binding.webView
        webView.webViewClient = WebViewClient()
        webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setSupportMultipleWindows(true)
        webSettings.useWideViewPort = true
        webSettings.setSupportZoom(true)
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
    }

    private fun loadEula() {
        binding.titleLayout.title = "이용약관"
        binding.textScrollView.visibility = View.GONE
        binding.webView.loadUrl("https://www.my-bury.com/terms_of_use")
    }

    private fun loadPrivacyPolicy() {
        binding.titleLayout.title = "개인정보 처리방침"
        binding.textScrollView.visibility = View.GONE
        binding.webView.loadUrl("https://www.my-bury.com/privacy_policy")
    }

    private fun loadOpenSourceText() {
        binding.titleLayout.title = "오픈 소스 라이선스"
        binding.webView.visibility = View.GONE
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
            binding.textView.text = Html.fromHtml(stringBuilder.toString())
        }
        `is`.close()
    }
}