package womenproject.com.mybury.presentation.mypage.appinfo

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import womenproject.com.mybury.R
import womenproject.com.mybury.data.DataTextType
import womenproject.com.mybury.databinding.FragmentAppInfoBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.util.Converter.Companion.stringFormat


/**
 * Created by HanAYeon on 2019-08-20.
 */

class AppInfoFragment : BaseFragment<FragmentAppInfoBinding, AppInfoViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_app_info

    override val viewModel: AppInfoViewModel
        get() = AppInfoViewModel()

    private lateinit var viewModelL: AppInfoViewModel

    override fun initDataBinding() {
        startLoading()
        viewModelL = ViewModelProvider(this).get(AppInfoViewModel::class.java)

        viewModelL.latelyVersion.observe(this, Observer {
            setUpViews()
            stopLoading()
        })
        viewModelL.getLatelyVersion()
    }

    private fun setUpViews() {
        viewDataBinding.backLayout.title = "앱 정보"
        viewDataBinding.useEula.content = "이용약관"
        viewDataBinding.privacyEula.content = "개인 정보 처리 방침"
        viewDataBinding.openSource.content = "오픈 소스 라이선스"

        viewDataBinding.currentVersionInfo.text = stringFormat(getString(R.string.app_current_version), viewModelL.currentVersion)

        if (viewModelL.currentVersion == viewModelL.latelyVersion.value) {
            viewDataBinding.versionText = "최신 버전 사용 중"
            viewDataBinding.updateBtn.isEnabled = false

        } else {
            viewDataBinding.versionText = "최신 버전 업데이트"
            viewDataBinding.updateBtn.isEnabled = true
        }

        viewDataBinding.backLayout.backBtnOnClickListener = backBtnOnClickListener()
        viewDataBinding.useEula.appInfoDetailClickListener = goToUseEula()
        viewDataBinding.privacyEula.appInfoDetailClickListener = goToPrivacy()
        viewDataBinding.openSource.appInfoDetailClickListener = goToOpenSource()
        viewDataBinding.updateBtn.setOnClickListener(goToPlayStore())
    }

    private fun goToUseEula() = View.OnClickListener {
        val directions = AppInfoFragmentDirections.actionInfoToDetail()
        directions.type = DataTextType.eula.toString()
        it.findNavController().navigate(directions)
    }

    private fun goToPrivacy() = View.OnClickListener {
        val directions = AppInfoFragmentDirections.actionInfoToDetail()
        directions.type = DataTextType.privacy.toString()
        it.findNavController().navigate(directions)
    }

    private fun goToOpenSource() = View.OnClickListener {
        val directions = AppInfoFragmentDirections.actionInfoToDetail()
        directions.type = DataTextType.openSource.toString()
        it.findNavController().navigate(directions)
    }

    private fun goToPlayStore() = View.OnClickListener {
        val appPackageName = "womenproject.com.mybury"
        try {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }
}