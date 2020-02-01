package womenproject.com.mybury.presentation.mypage.appinfo

import womenproject.com.mybury.BuildConfig
import womenproject.com.mybury.presentation.base.BaseViewModel

/**
 * Created by HanAYeon on 2019-08-20.
 */

class AppInfoViewModel : BaseViewModel() {

    val currentVersion = BuildConfig.VERSION_NAME
    val latelyVersion = "1.0.1"

}