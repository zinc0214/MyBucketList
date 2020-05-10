package womenproject.com.mybury.presentation.mypage.appinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import womenproject.com.mybury.BuildConfig
import womenproject.com.mybury.presentation.base.BaseViewModel
import java.io.IOException

/**
 * Created by HanAYeon on 2019-08-20.
 */

class AppInfoViewModel : BaseViewModel() {

    private val _latelyVersion = MutableLiveData<String>()

    val currentVersion = BuildConfig.VERSION_NAME
    val latelyVersion: LiveData<String> = _latelyVersion
    var latelyVersionS = ""

    fun getLatelyVersion() {
        GlobalScope.launch {
            try {
                val doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "womenproject.com.mybury").get()
                val Version = doc.select(".htlgb").eq(7)
                for (mElement in Version) {
                    latelyVersionS = mElement.text().trim { it <= ' ' }
                    withContext(Dispatchers.Main) {
                        _latelyVersion.value = latelyVersionS
                    }
                }
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }
}