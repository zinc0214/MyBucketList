package womenproject.com.mybury.presentation.mypage.appinfo

import android.graphics.Rect
import android.text.Html
import android.view.ViewTreeObserver
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.FragmentAppInfoBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.util.Converter.Companion.stringFormat

/**
 * Created by HanAYeon on 2019-08-20.
 */

class AppInfoFragment :  BaseFragment<FragmentAppInfoBinding, AppInfoViewModel>() {


    override val layoutResourceId: Int
        get() = R.layout.fragment_app_info

    override val viewModel: AppInfoViewModel
        get() = AppInfoViewModel()


    override fun initDataBinding() {
        viewDataBinding.backLayout.title = "앱 정보"
        viewDataBinding.useEula.content = "이용약관"
        viewDataBinding.privacyEula.content = "개인 정보 처리 방침"
        viewDataBinding.openSource.content = "오픈 소스 라이선스"
        viewDataBinding.howToUse.content = "도움말"

        viewDataBinding.currentVersionInfo.text = stringFormat(getString(R.string.app_current_version), viewModel.currentVersion)

        if(viewModel.currentVersion.equals(viewModel.latelyVersion)) {
            viewDataBinding.versionText = "최신 버전 사용 중"
            viewDataBinding.updateBtn.isEnabled = false

        } else {
            viewDataBinding.versionText = "최신 버전 업데이트"
            viewDataBinding.updateBtn.isEnabled = true
        }

        viewDataBinding.backLayout.backBtnOnClickListener = setOnBackBtnClickListener()
    }

}