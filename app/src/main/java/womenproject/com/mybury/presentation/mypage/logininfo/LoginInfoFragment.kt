package womenproject.com.mybury.presentation.mypage.logininfo

import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.FragmentLoginInfoBinding
import womenproject.com.mybury.presentation.base.BaseFragment

/**
 * Created by HanAYeon on 2019-09-16.
 */

class LoginInfoFragment : BaseFragment<FragmentLoginInfoBinding, LoginInfoViewModel>() {


    override val layoutResourceId: Int
        get() = R.layout.fragment_login_info

    override val viewModel: LoginInfoViewModel
        get() = LoginInfoViewModel()


    override fun initDataBinding() {
        viewDataBinding.backLayout.title = "로그인 정보"
        viewDataBinding.backLayout.backBtnOnClickListener = setOnBackBtnClickListener()
    }

}
