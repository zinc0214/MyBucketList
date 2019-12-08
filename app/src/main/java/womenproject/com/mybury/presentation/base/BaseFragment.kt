package womenproject.com.mybury.presentation.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import womenproject.com.mybury.presentation.MainActivity

/**
 * Created by HanAYeon on 2019. 3. 7..
 */

abstract class BaseFragment<T : ViewDataBinding, R : BaseViewModel> : Fragment() {

    lateinit var viewDataBinding: T

    abstract val layoutResourceId : Int

    abstract val viewModel : R

    /*
    * 두번째 호출
    * 데이터 바인딩, rxJava 설정
    * */
    abstract fun initDataBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.addOnBackPressedCallback(this, OnBackPressedCallback {
            Log.e("ayhan", "backBtn")
            setOnBackBtnClickListener()
            false
        })
        checkNetworkConnect()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)


        return viewDataBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initDataBinding()
    }

    private fun checkNetworkConnect(){
        if(BaseViewModel().isNetworkDisconnect()) {

            BaseNetworkFragment().show(activity!!.supportFragmentManager, "tag")

        }
    }


    class BaseNetworkFragment : BaseNormalDialogFragment() {

        init {
            TITLE_MSG = "네트워크 알림"
            CONTENT_MSG = "네트워크 연결이 끊겼습니다."
            CANCEL_BUTTON_VISIBLE = false
            GRADIENT_BUTTON_VISIBLE = true
            CONFIRM_TEXT = "확인"
        }
    }

    open fun setOnBackBtnClickListener() : View.OnClickListener {
        return View.OnClickListener {
            activity!!.onBackPressed()
        }
    }

    fun startLoading() {
        Log.e("ayhan", "startLoading")
        if(activity is MainActivity) {
            val a = activity as MainActivity
            a.startLoading()
            Log.e("ayhan", "startLoading2")
        }

    }

    fun stopLoading() {
        if(activity is MainActivity) {
            val a = activity as MainActivity
            a.stopLoading()
        }
    }

}
