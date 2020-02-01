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
import womenproject.com.mybury.presentation.MainActivity

/**
 * Created by HanAYeon on 2019. 3. 7..
 */

abstract class BaseFragment<T : ViewDataBinding, R : BaseViewModel> : Fragment() {

    lateinit var viewDataBinding: T

    abstract val layoutResourceId: Int

    abstract val viewModel: R

    abstract fun initDataBinding()

    var isCancelConfirm = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.addOnBackPressedCallback(this, OnBackPressedCallback {
            Log.e("ayhan", "softBackBtnClick")
            if (isCancelConfirm) {
                false
            } else {
                actionByBackButton()
                true
            }
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

    private fun checkNetworkConnect() {
        if (BaseViewModel().isNetworkDisconnect()) {
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

    fun backBtnOnClickListener() = View.OnClickListener {
        Log.e("ayhan", "backBtnOnClickListener")
        activity!!.onBackPressed()
    }

    fun onBackPressedFragment() {
        isCancelConfirm = true
        activity!!.onBackPressed()
    }

    open fun actionByBackButton() {
        Log.e("ayhan", "actionByBackButton")
    }

    fun startLoading() {
        Log.e("ayhan", "startLoading")
        if (activity is MainActivity) {
            val a = activity as MainActivity
            a.startLoading()
            Log.e("ayhan", "startLoading2")
        }
    }

    fun stopLoading() {
        if (activity is MainActivity) {
            val a = activity as MainActivity
            a.stopLoading()
        }
    }
}
