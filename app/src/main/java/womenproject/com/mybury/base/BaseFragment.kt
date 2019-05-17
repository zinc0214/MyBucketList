package womenproject.com.mybury.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import womenproject.com.mybury.MyBuryApplication

/**
 * Created by HanAYeon on 2019. 3. 7..
 */

abstract class BaseFragment<T : ViewDataBinding, R : BaseViewModel> : Fragment() {

    lateinit var viewDataBinding: T

    abstract val layoutResourceId : Int

    abstract val viewModel : R

    /*
    * 레이아웃을 띄운 직후 호출
    * 뷰, 액티비티 속성을 초기화 함
    * ex. RecylcerView
    * */
    abstract fun initStartView()


    /*
    * 두번째 호출
    * 데이터 바인딩, rxJava 설정
    * */
    abstract fun initDataBinding()


    /*
    * 바인딩 후 할 일들을 구현
    * 클릭 리스너도 이곳에서 구햔
    * */
    abstract fun initAfterBinding()


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

        initStartView()
        initDataBinding()
        initAfterBinding()
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

}

