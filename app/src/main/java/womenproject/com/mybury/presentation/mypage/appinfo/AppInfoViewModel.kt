package womenproject.com.mybury.presentation.mypage.appinfo

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.BuildConfig
import womenproject.com.mybury.data.MyPageInfo
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

/**
 * Created by HanAYeon on 2019-08-20.
 */

class AppInfoViewModel : BaseViewModel() {

    val currentVersion = BuildConfig.VERSION_NAME
    val latelyVersion = "1.0.1"

}