package womenproject.com.mybury.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

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

    fun setOnBackBtnClickListener() : View.OnClickListener {
        return View.OnClickListener {
            activity!!.onBackPressed()
        }
    }

}

