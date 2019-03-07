package womenproject.com.mybury.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

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

        initStartView()
        initDataBinding()
        initAfterBinding()

        return viewDataBinding.root
    }


    private fun checkNetworkConnect(){
        if(BaseViewModel().isNetworkDisconnect()) {

            // Dialog호출

        }
    }

}

