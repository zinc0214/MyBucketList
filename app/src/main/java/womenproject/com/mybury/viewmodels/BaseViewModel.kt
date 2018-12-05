package womenproject.com.mybury.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.ViewModel
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.view.BaseDialog
import womenproject.com.mybury.view.MainActivity


/**
 * Created by HanAYeon on 2018. 11. 26..
 */

open class BaseViewModel : ViewModel() {

    init{
        if(checkNetwork()) {
            Toast.makeText(MyBuryApplication.context, "Network1", Toast.LENGTH_SHORT).show()
//            BaseDialog().showDialog()
        } else {
            Toast.makeText(MyBuryApplication.context, "Network2", Toast.LENGTH_SHORT).show()
//            BaseDialog().showDialog()
        }
    }

    private fun checkNetwork() : Boolean {
        val cm = MyBuryApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

       return isConnected

      //  return true
    }
}