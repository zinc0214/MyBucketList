package womenproject.com.mybury.presentation

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import womenproject.com.mybury.BuildConfig
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.databinding.ActivityMainBinding
import womenproject.com.mybury.presentation.base.BaseActiviy
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.util.ScreenUtils.Companion.setStatusBar


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

    private lateinit var interstitialAd: InterstitialAd

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

        Preference.setEnableShowAlarm(this, true)
        setStatusBar(this, R.color._ffffff)

        initAdMob()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initAdMob() {

        MobileAds.initialize(this) { }
        interstitialAd = InterstitialAd(this)

        //"ca-app-pub-3940256099942544/1033173712" 는 구글에서 테스트용으로 공개한 Test Ad Id - 배포시 실제 Id 로 변경 해야 함
        /*
            @안드로이드용 테스트광고 ID -- (https://developers.google.com/admob/unity/test-ads?hl=ko)
            - 배너 광고: ca-app-pub-3940256099942544/6300978111
            - 전면 광고: ca-app-pub-3940256099942544/1033173712
            - 보상형 동영상 광고: ca-app-pub-3940256099942544/5224354917
            - 네이티브 광고 고급형: ca-app-pub-3940256099942544/2247696110
        */

        //"ca-app-pub-3940256099942544/1033173712" 는 구글에서 테스트용으로 공개한 Test Ad Id - 배포시 실제 Id 로 변경 해야 함
        /*
            @안드로이드용 테스트광고 ID -- (https://developers.google.com/admob/unity/test-ads?hl=ko)
            - 배너 광고: ca-app-pub-3940256099942544/6300978111
            - 전면 광고: ca-app-pub-3940256099942544/1033173712
            - 보상형 동영상 광고: ca-app-pub-3940256099942544/5224354917
            - 네이티브 광고 고급형: ca-app-pub-3940256099942544/2247696110
        */

        interstitialAd.adUnitId = if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1033173712"
        else "ca-app-pub-6302671173915322/8119781606"

        //광고 이벤트리스너 등록

        //광고 이벤트리스너 등록
        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d("ayhan", "onAdLoaded")
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                Log.d("ayhan", "onAdFailedToLoad")
            }

            override fun onAdOpened() {
                Log.d("ayhan", "onAdOpened")
            }

            override fun onAdClicked() {
                Log.d("ayhan", "onAdClicked")
            }

            override fun onAdLeftApplication() {
                Log.d("ayhan", "onAdLeftApplication")
            }

            override fun onAdClosed() {
                Log.d("ayhan", "onAdClosed")
                // Code to be executed when the interstitial ad is closed.
                interstitialAd.loadAd(AdRequest.Builder().build())
            }
        }

        interstitialAd.loadAd(AdRequest.Builder().build())
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

        if (interstitialAd.isLoaded) {
            interstitialAd.show()
        } else {
            Log.d("ayhan", "The interstitial wasn't loaded yet.");
        }

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