package womenproject.com.mybury.presentation.mypage.logininfo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference.Companion.allClear
import womenproject.com.mybury.data.Preference.Companion.clearAllLocalInfo
import womenproject.com.mybury.data.Preference.Companion.getAccountEmail
import womenproject.com.mybury.data.Preference.Companion.setMyBuryLoginComplete
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.databinding.FragmentLoginInfoBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.intro.SplashLoginActivity
import womenproject.com.mybury.presentation.viewmodels.MyPageViewModel
import womenproject.com.mybury.util.observeNonNull

/**
 * Created by HanAYeon on 2019-09-16.
 */

@AndroidEntryPoint
class LoginInfoFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginInfoBinding
    private val myPageViewModel by viewModels<MyPageViewModel>()

    private var isSuccessDelete = false
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_info, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataBinding()
        setUpObservers()
    }

    private fun initDataBinding() {
        binding.backLayout.title = "로그인 정보"
        binding.backLayout.backBtnOnClickListener = backBtnOnClickListener()
        binding.activity = this
    }

    private fun setUpObservers() {
        myPageViewModel.deleteAccountEvent.observeNonNull(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> {
                    // DO Nothing...
                }
                LoadState.RESTART -> signOutMyBury()
                LoadState.SUCCESS -> {
                    allClear(requireContext())
                    isSuccessDelete = true
                }
                LoadState.FAIL -> {
                    LogoutOrSignOutFailed("계정삭제 실패").show(
                        requireActivity().supportFragmentManager,
                        "tag"
                    )
                }
            }
        }
    }

    fun getLoginText() = "${getAccountEmail(requireContext())} 계정으로\n로그인하였습니다."

    fun accountDeleteClickListener() {
        val startDeleting: () -> Unit = {
            signOutMyBury()
        }

        val animEnd: () -> Unit = {
            if (isSuccessDelete) {
                val intent = Intent(context, SplashLoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                MyBuryApplication.context.startActivity(intent)
                this.activity?.finish()
            }
        }

        AccountDeleteDialogFragment(
            startDeleting,
            animEnd
        ).show(requireActivity().supportFragmentManager, "tag")
    }

    fun googleLogoutClickListener() {
        initGoogle()
        signOut()
    }

    private fun signOutMyBury() {
        myPageViewModel.deleteAccount()
    }

    private fun initGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        auth = FirebaseAuth.getInstance()

    }

    private fun signOut() {
        auth.signOut()

        googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
            setMyBuryLoginComplete(requireContext(), false)
            clearAllLocalInfo(requireContext())
            LogoutSuccess().show(requireActivity().supportFragmentManager)
        }
    }
}


class LogoutSuccess : BaseNormalDialogFragment() {

    init {
        TITLE_MSG = "로그아웃 완료"
        CONTENT_MSG = "로그아웃이 완료되었습니다."
        CANCEL_BUTTON_VISIBLE = false
        GRADIENT_BUTTON_VISIBLE = true
        CONFIRM_TEXT = "확인"
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            dismiss()
            val intent = Intent(context, SplashLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            MyBuryApplication.context.startActivity(intent)
            this.activity?.finish()
        }
    }

}


class LogoutOrSignOutFailed(title: String) : BaseNormalDialogFragment() {

    init {
        TITLE_MSG = title
        CONTENT_MSG = "계정 삭제에 실패했습니다.\n다시 시도해주세요."
        CANCEL_BUTTON_VISIBLE = false
        GRADIENT_BUTTON_VISIBLE = true
        CONFIRM_TEXT = "확인"
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            dismiss()
        }
    }

}
