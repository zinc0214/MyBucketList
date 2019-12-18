package womenproject.com.mybury.presentation.mypage.appinfo

import android.util.Log
import android.webkit.*
import womenproject.com.mybury.R
import womenproject.com.mybury.data.DataTextType
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
import womenproject.com.mybury.databinding.TextViewLayoutBinding
import womenproject.com.mybury.presentation.base.BaseFragment

/**
 * Created by HanAYeon on 2019-08-20.
 */

class AppInfoTextFragment : BaseFragment<TextViewLayoutBinding, AppInfoViewModel>() {


    override val layoutResourceId: Int
        get() = R.layout.text_view_layout

    override val viewModel: AppInfoViewModel
        get() = AppInfoViewModel()

    private lateinit var type: String
    private lateinit var webSettings: WebSettings
    private lateinit var webView : WebView

    override fun initDataBinding() {
        arguments?.let {
            val args = AppInfoTextFragmentArgs.fromBundle(it)
            val textType = args.type
            type = textType!!
        }

        viewDataBinding.titleLayout.backBtnOnClickListener = setOnBackBtnClickListener()

        when (type) {
            DataTextType.eula.toString() -> loadEula()
            DataTextType.privacy.toString() -> loadPrivacyPolicy()
        }
    }


    private fun initWebView() {

        startLoading()

        webView = viewDataBinding.webView
        webView.settings.javaScriptEnabled = true
        webView.settings.useWideViewPort = true
        webView.webViewClient = (object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url : String): Boolean {
                view!!.loadUrl(url)
                stopLoading()
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                stopLoading()
            }
        })
    }

    private fun loadEula() {
        viewDataBinding.titleLayout.title = "이용약관"
        initWebView()
        viewDataBinding.webView.loadUrl("www.naver.com")
    }

    private fun loadPrivacyPolicy() {
        viewDataBinding.titleLayout.title = "개인정보 처리방침"
        viewDataBinding.webView.loadUrl("www.my-bury.com/privacy_policy")
    }


}