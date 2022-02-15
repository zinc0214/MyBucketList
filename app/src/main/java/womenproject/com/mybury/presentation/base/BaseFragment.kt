package womenproject.com.mybury.presentation.base

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.data.SupportInfo
import womenproject.com.mybury.presentation.MainActivity
import javax.inject.Inject


/**
 * Created by HanAYeon on 2019. 3. 7..
 */

@AndroidEntryPoint
open class BaseFragment @Inject constructor(): Fragment() {

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
            a.setAdShowable(price)
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
