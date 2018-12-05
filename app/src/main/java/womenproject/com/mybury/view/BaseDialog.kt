package womenproject.com.mybury.view

import android.app.AlertDialog
import womenproject.com.mybury.MyBuryApplication

/**
 * Created by HanAYeon on 2018. 12. 4..
 */

class BaseDialog {

    fun showDialog() {

        var alertBuilder = AlertDialog.Builder(MyBuryApplication.context)
        alertBuilder.setTitle("Network is not connect.")
        alertBuilder.setMessage("Please Check your Connect State")
        alertBuilder.setNeutralButton("Ok") { dialog, which ->
            // Go To wifi
        }
        alertBuilder.show()
    }
}