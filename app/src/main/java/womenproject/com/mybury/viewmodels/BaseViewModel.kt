package womenproject.com.mybury.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.ViewModel
import io.reactivex.subjects.BehaviorSubject
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.R
import womenproject.com.mybury.util.ContextContainer


/**
 * Created by HanAYeon on 2018. 11. 26..
 */

open class BaseViewModel : ViewModel() {

    init {
        if (checkNetwork()) {
            Toast.makeText(MyBuryApplication.context, "Network Is Connect", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(MyBuryApplication.context, "Network Is Not Connect", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkNetwork(): Boolean {
        val cm = MyBuryApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        return true
    }



}