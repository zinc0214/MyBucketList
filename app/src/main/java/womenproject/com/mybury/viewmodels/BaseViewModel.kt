package womenproject.com.mybury.viewmodels

import androidx.lifecycle.ViewModel
import womenproject.com.mybury.util.NetworkUtil

/**
 * Created by HanAYeon on 2018. 11. 26..
 */

open class BaseViewModel : ViewModel() {

    init{

        if(!isNetworkConnect()) {

        }
    }

    fun isNetworkConnect() : Boolean {

        return NetworkUtil.isOnline()
    }
}