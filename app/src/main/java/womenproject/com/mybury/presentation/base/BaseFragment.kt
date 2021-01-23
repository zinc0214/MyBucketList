package womenproject.com.mybury.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import womenproject.com.mybury.data.SupportInfo
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

    lateinit var goToBackCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goToBackCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                //do when go to back.
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, goToBackCallback)

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
            BaseNetworkFragment().show(requireActivity().supportFragmentManager, "tag")
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
        requireActivity().onBackPressed()
    }

    fun onBackPressedFragment() {
        isCancelConfirm = true
        requireActivity().onBackPressed()
    }

    open fun actionByBackButton() {
        // back Button Action
    }

    fun startLoading() {

        if (activity is MainActivity) {
            val a = activity as MainActivity
            a.startLoading()
        }
    }

    fun stopLoading() {
        if (activity is MainActivity) {
            val a = activity as MainActivity
            a.stopLoading()
        }
    }

    fun startAdMob() {
        if (activity is MainActivity) {
            val a = activity as MainActivity
            a.showAds()
        }
    }

    fun setCurrentSupportPrice(price: Int) {
        if (activity is MainActivity) {
            val a = activity as MainActivity
            a.setSupportPrice(price)
        }
    }


    fun getSupportInfo(): SupportInfo? {
        return if (activity is MainActivity) {
            val a = activity as MainActivity
            if (a.supportInfo == null) null
            else a.supportInfo
        } else null
    }

    fun purchaseSelectItem(id: String, purchaseSuccess: () -> Unit, purchaseFail: () -> Unit) {
        if (activity is MainActivity) {
            val a = activity as MainActivity
            a.purchaseSelectItem(id, purchaseSuccess, purchaseFail)
        }
    }


}
