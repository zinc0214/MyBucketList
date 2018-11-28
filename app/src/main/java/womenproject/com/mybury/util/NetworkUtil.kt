package womenproject.com.mybury.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by HanAYeon on 2018. 11. 26..
 */

object NetworkUtil {

    private val applicationContext by lazy { ContextContainer.instance.getApplicationContext() }
    private val connectivityManager by lazy { applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    fun getActiveNetworkInfo(): NetworkInfo? = connectivityManager.activeNetworkInfo

    fun isActiveNetworkConnected(): Boolean = getActiveNetworkInfo()?.isConnected == true

    fun isOnline(): Boolean = isActiveNetworkConnected()

    @JvmOverloads
    fun isConnectedViaMobileNetwork(info: NetworkInfo? = getActiveNetworkInfo()): Boolean {
        info ?: return false
        return info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
    }

    @JvmOverloads
    fun isConnectedViaWifi(info: NetworkInfo? = getActiveNetworkInfo()): Boolean {
        info ?: return false
        return info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
    }

    @JvmOverloads
    fun isConnectedViaEthernet(info: NetworkInfo? = getActiveNetworkInfo()): Boolean {
        info ?: return false
        return info.isConnected && info.type == ConnectivityManager.TYPE_ETHERNET
    }

}
