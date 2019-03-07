package womenproject.com.mybury.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import womenproject.com.mybury.R
import womenproject.com.mybury.viewmodels.BaseViewModel

/**
 * Created by HanAYeon on 2018. 12. 5..
 */

open class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkNetworkConnect()
    }

    private fun checkNetworkConnect(){
        if(BaseViewModel().isNetworkDisconnect()) {


        }
    }

}