package womenproject.com.mybury.presentation.mypage.appinfo

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.data.WebViewType
import womenproject.com.mybury.databinding.FragmentAppInfoBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.util.Converter.Companion.stringFormat


/**
 * Created by HanAYeon on 2019-08-20.
 */

@AndroidEntryPoint
class AppInfoFragment : BaseFragment() {

    private lateinit var binding : FragmentAppInfoBinding
    
    private val viewModel by viewModels<AppInfoViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,  R.layout.fragment_app_info, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initDataBinding()
    }

    private fun initDataBinding() {
        startLoading()

        viewModel.latelyVersion.observe(this) {
            setUpViews()
            stopLoading()
        }
        viewModel.getLatelyVersion()
    }

    private fun setUpViews() {
        binding.backLayout.title = "앱 정보"
        binding.useEula.content = "이용약관"
        binding.privacyEula.content = "개인 정보 처리 방침"
        binding.openSource.content = "오픈 소스 라이선스"

        binding.currentVersionInfo.text = stringFormat(getString(R.string.app_current_version), viewModel.currentVersion)

        if (viewModel.currentVersion == viewModel.latelyVersion.value) {
            binding.versionText = "최신 버전 사용 중"
            binding.updateBtn.isEnabled = false

        } else {
            binding.versionText = "최신 버전 업데이트"
            binding.updateBtn.isEnabled = true
        }

        binding.backLayout.backBtnOnClickListener = backBtnOnClickListener()
        binding.useEula.appInfoDetailClickListener = goToUseEula()
        binding.privacyEula.appInfoDetailClickListener = goToPrivacy()
        binding.openSource.appInfoDetailClickListener = goToOpenSource()
        binding.updateBtn.setOnClickListener(goToPlayStore())
    }

    private fun goToUseEula() = View.OnClickListener {
        val directions = AppInfoFragmentDirections.actionInfoToDetail()
        directions.type = WebViewType.eula.toString()
        it.findNavController().navigate(directions)
    }

    private fun goToPrivacy() = View.OnClickListener {
        val directions = AppInfoFragmentDirections.actionInfoToDetail()
        directions.type = WebViewType.privacy.toString()
        it.findNavController().navigate(directions)
    }

    private fun goToOpenSource() = View.OnClickListener {
        val directions = AppInfoFragmentDirections.actionInfoToDetail()
        directions.type = WebViewType.openSource.toString()
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