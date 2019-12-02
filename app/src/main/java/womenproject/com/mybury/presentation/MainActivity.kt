package womenproject.com.mybury.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.R
import womenproject.com.mybury.data.GetTokenRequest
import womenproject.com.mybury.data.GetTokenResponse
import womenproject.com.mybury.data.Preference.Companion.getUserId
import womenproject.com.mybury.data.Preference.Companion.setAccessToken
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.databinding.ActivityMainBinding
import womenproject.com.mybury.presentation.base.BaseActiviy
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.intro.CancelDialog

/**
 * Created by HanAYeon on 2018. 11. 26..
 */

class MainActivity : BaseActiviy() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToken()

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        navController = Navigation.findNavController(this, R.id.nav_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        val baseViewModel = BaseViewModel()

        if(baseViewModel.isNetworkDisconnect()) {
            NetworkFailDialog().show(supportFragmentManager, "tag")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    fun showDialog() {
        //  BaseNormalDialogFragment().showDialog()
    }

    @SuppressLint("CheckResult")
    private fun initToken() {

        val getTokenRequest = GetTokenRequest(getUserId(this))

        apiInterface.getLoginToken(getTokenRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if(response.retcode!="200") {
                        CanNotGoMainDialog().show(supportFragmentManager, "tag")
                    } else {
                        setAccessToken(this, response.accessToken)
                    }

                }) {
                    Log.e("ayhan", it.toString())
                    CanNotGoMainDialog().show(supportFragmentManager, "tag")
                }


    }

}

class NetworkFailDialog : BaseNormalDialogFragment() {

    init {
        TITLE_MSG = "네트워크 통신 실패"
        CONTENT_MSG = "네트워크가 불완전합니다. 다시 시도해주세요/"
        CANCEL_BUTTON_VISIBLE = false
        GRADIENT_BUTTON_VISIBLE = true
        CONFIRM_TEXT = "확인"
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            dismiss()
            activity!!.finish()
        }
    }

}

class CanNotGoMainDialog : BaseNormalDialogFragment() {

    init {
        TITLE_MSG = "로그인 불가"
        CONTENT_MSG = "로그인에 실패했습니다. 다시 시도해주세요."
        CANCEL_BUTTON_VISIBLE = false
        GRADIENT_BUTTON_VISIBLE = true
        CONFIRM_TEXT = "확인"
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            dismiss()
            activity!!.finish()
        }
    }

}