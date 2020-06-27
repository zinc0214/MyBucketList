package womenproject.com.mybury.presentation

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference.Companion.setAleadyAlarmShow
import womenproject.com.mybury.databinding.ActivityMainBinding
import womenproject.com.mybury.presentation.base.BaseActiviy
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.base.BaseViewModel

/**
 * Created by HanAYeon on 2018. 11. 26..
 */

class MainActivity : BaseActiviy() {


    private lateinit var binding: ActivityMainBinding

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var loadingImg: ImageView
    private lateinit var animationDrawable: AnimationDrawable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        navController = Navigation.findNavController(this, R.id.nav_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        val baseViewModel = BaseViewModel()

        if (baseViewModel.isNetworkDisconnect()) {
            NetworkFailDialog().show(supportFragmentManager, "tag")
        }

        loadingImg = binding.loadingLayout.loadingImg
        loadingImg.setImageResource(R.drawable.loading_anim)
        animationDrawable = loadingImg.drawable as AnimationDrawable

        setAleadyAlarmShow(this, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    public fun startLoading() {
        animationDrawable.start()
        binding.loadingLayout.layout.visibility = View.VISIBLE
    }

    public fun stopLoading() {
        animationDrawable.stop()
        binding.loadingLayout.layout.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}

class NetworkFailDialog : BaseNormalDialogFragment() {

    init {
        TITLE_MSG = "네트워크 통신 실패"
        CONTENT_MSG = "네트워크가 불완전합니다. 다시 시도해주세요."
        CANCEL_BUTTON_VISIBLE = false
        GRADIENT_BUTTON_VISIBLE = true
        CONFIRM_TEXT = "확인"
        CANCEL_ABLE = false
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            dismiss()
            requireActivity().finish()
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
        CANCEL_ABLE = false
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            dismiss()
            requireActivity().finish()
        }
    }
}


class UserAlreadyExist : BaseNormalDialogFragment() {

    init {
        TITLE_MSG = "로그인 불가"
        CONTENT_MSG = "이미 생성되어있는 계정입니다."
        CANCEL_BUTTON_VISIBLE = false
        GRADIENT_BUTTON_VISIBLE = true
        CONFIRM_TEXT = "확인"
        CANCEL_ABLE = false
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            dismiss()
            requireActivity().finish()
        }
    }
}