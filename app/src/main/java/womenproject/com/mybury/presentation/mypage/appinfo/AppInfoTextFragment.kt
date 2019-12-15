package womenproject.com.mybury.presentation.mypage.appinfo
import android.util.Log
import womenproject.com.mybury.R
import womenproject.com.mybury.data.DataTextType
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
import womenproject.com.mybury.databinding.TextViewLayoutBinding
import womenproject.com.mybury.presentation.base.BaseFragment

/**
 * Created by HanAYeon on 2019-08-20.
 */

class AppInfoTextFragment :  BaseFragment<TextViewLayoutBinding, AppInfoViewModel>() {


    override val layoutResourceId: Int
        get() = R.layout.text_view_layout

    override val viewModel: AppInfoViewModel
        get() = AppInfoViewModel()

    private lateinit var type : String
    private lateinit var tokenId : String

    override fun initDataBinding() {

        tokenId = getAccessToken(context!!)

        arguments?.let {
            val args = AppInfoTextFragmentArgs.fromBundle(it)
            val textType = args.type
            type = textType!!
        }

        viewDataBinding.titleLayout.backBtnOnClickListener = setOnBackBtnClickListener()

        when(type) {
            DataTextType.eula.toString() -> loadEula()
            DataTextType.privacy.toString() -> loadPrivacyPolicy()
        }
    }

private fun loadEula() {
        viewModel.loadTermsOfUse(object : AppInfoViewModel.LoadTextView {
            override fun start() {
                startLoading()
            }

            override fun success(text: String) {
                Log.e("ayhan", "eulaResult : $text")
                viewDataBinding.textField.text = text
                viewDataBinding.titleLayout.title = "이용약관"
                stopLoading()
                viewDataBinding.notifyChange()
            }

            override fun fail() {
                stopLoading()
            }

        }, tokenId)
    }

    private fun loadPrivacyPolicy() {
        viewModel.loadPrivacyPolicy(object : AppInfoViewModel.LoadTextView {
            override fun start() {
                startLoading()
            }

            override fun success(text: String) {
                Log.e("ayhan", "eulaResult : $text")
                viewDataBinding.textField.text = text
                viewDataBinding.titleLayout.title = "개인 정보 처리 방침"
                stopLoading()
                viewDataBinding.notifyChange()
            }

            override fun fail() {
                stopLoading()
            }

        }, tokenId)
    }


}