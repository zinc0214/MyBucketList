package womenproject.com.mybury.presentation

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.android.billingclient.api.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import womenproject.com.mybury.BuildConfig
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.PurchasableItem
import womenproject.com.mybury.data.SupportInfo
import womenproject.com.mybury.databinding.ActivityMainBinding
import womenproject.com.mybury.presentation.base.BaseActiviy
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.main.MainFragment
import womenproject.com.mybury.presentation.main.MainFragmentDirections
import womenproject.com.mybury.presentation.main.SupportDialogFragment
import womenproject.com.mybury.presentation.mypage.MyPageFragmentDirections
import womenproject.com.mybury.presentation.viewmodels.MyBurySupportViewModel
import womenproject.com.mybury.util.ScreenUtils.Companion.setStatusBar


/**
 * Created by HanAYeon on 2018. 11. 26..
 */

class MainActivity : BaseActiviy(), PurchasesUpdatedListener, PurchaseHistoryResponseListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var supportViewModel: MyBurySupportViewModel

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var loadingImg: ImageView
    private lateinit var animationDrawable: AnimationDrawable

    private lateinit var interstitialAd: InterstitialAd
    private var isAdShow = true

    private lateinit var billingClient: BillingClient

    private val purchasableItemIds = ArrayList<String>()
    private val purchasedItemIds = ArrayList<String>()
    private var purchasedItem: PurchasableItem? = null


    public var supportInfo: SupportInfo? = null
    public lateinit var purchaseSuccess: (String) -> Unit
    public lateinit var purchaseFail: () -> Unit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        navController = Navigation.findNavController(this, R.id.nav_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        val baseViewModel = BaseViewModel()
        supportViewModel = MyBurySupportViewModel()

        if (baseViewModel.isNetworkDisconnect()) {
            NetworkFailDialog().show(supportFragmentManager, "tag")
        }

        supportViewModel.getPurchasableItem {
            // Do Something
        }

        supportViewModel.supportInfo.observe(this, Observer { info ->
            info.totalPrice.let {
                isAdShow = it.toInt() < SUPPORT_PRICE
            }
            initBillingClient(info.supportItems)
            supportInfo = info
            supportInfo?.supportItems?.forEach {
                it.isPurchasable = true
            }
        })

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
                showSupportDialogFragment()
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

    public fun showAds() {
        Log.e("ayhan", "isAdShow : $isAdShow")
        if (interstitialAd.isLoaded && isAdShow) {
            interstitialAd.show()
        } else {
            Log.d("ayhan", "The interstitial wasn't loaded yet.");
        }
    }

    fun setSupportPrice(price: Int) {
        isAdShow = price < SUPPORT_PRICE
    }

    fun purchaseSelectItem(id: String, purchaseSuccess: (String) -> Unit, purchaseFail: () -> Unit) {
        purchaseItem(id)
        this.purchaseSuccess = purchaseSuccess
        this.purchaseFail = purchaseFail
    }


    private fun showSupportDialogFragment() {
        val fragment = SupportDialogFragment()
        fragment.setButtonAction({
            purchaseItem("cheer_11000")
            fragment.dismiss()
        }, {
            val parentFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment)
            val currentFragment = parentFragment?.childFragmentManager?.fragments?.get(0)

            if (currentFragment is MainFragment) {
                val directions = MainFragmentDirections.actionMainBucketToMyburySupport()
                findNavController(R.id.nav_fragment).navigate(directions)
            } else {
                val directions = MyPageFragmentDirections.actionMyPageToMyburySupport()
                findNavController(R.id.nav_fragment).navigate(directions)
            }

            fragment.dismiss()

        })
        fragment.show(supportFragmentManager, "tag")
    }

    /**
     *  BillingClient 초기화
     */
    private fun initBillingClient(items: List<PurchasableItem>) {
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this)
                .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    getAcknowledgePurchasedItem()
                    getAllPurchasedItem()
                    setPurchasableList(items)
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.e("mybury", "Service Disconnected.")
            }
        })
    }

    /**
     * 이미 결제한 적이 있는 아이템을 보여주지 않아야 하는 경우 또는
     * 선택을 비활성화 시켜야 하는 경우 등에 사용할 function
     * acknowledgePurchase 을 통해 구매된 제품만 확인할 수 있다.
     */
    fun getAcknowledgePurchasedItem() {

        // BillingClient 의 준비가 되지않은 상태라면 돌려보낸다
        if (!billingClient.isReady) {
            return
        }

        // 인앱결제된 내역을 확인한다
        val result = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
        if (result.purchasesList == null) {
            Log.d("mybury", "No existing in app purchases found.")
        } else {
            Log.d("mybury", "Existing Once Type Item Bought purchases: ${result.purchasesList}")
            result.purchasesList?.forEach {
                //결쩨된 내역에 대한 처리
                purchasedItemIds.add(it.sku)
            }
        }
    }


    /**
     * 최근에 결제한 아이템을 확인하는 목적
     * checkPurchaseHistory function 과 다르게  consumeAsync 를 통해 구매된 상품도 확인할 수 있다.
     * !!주의!! 아이템 Id 로만 가져오기 때문에 여러번 구매하더라도 가장 최근의 제품만 가져온다.
     */
    fun getAllPurchasedItem() {
        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, this)
    }

    /**
     * 구매 가능한 리스트의 아이템을 추가한다.
     */
    private fun setPurchasableList(list: List<PurchasableItem>) {

        // Google PlayConsole 의 상품Id 와 동일하게 적어준다
        list.forEach { item ->
            if (purchasedItemIds.none { it == item.googleKey }) {
                purchasableItemIds.add(item.googleKey)
            }
        }
        purchasedItemCheck()
    }

    private fun purchasedItemCheck() {
        purchasedItemIds.forEach { purchasedId ->
            supportInfo?.supportItems?.filter { it.googleKey == purchasedId }?.forEach {
                it.isPurchasable = false
            }
        }
    }


    /**
     * 아이템을 구매하기 위해서는 구매가능한 아이템 리스트와 확인이 필요하다.
     * 리스트가 존재할 경우 실제 구매를 할 수 있다.
     */
    private fun purchaseItem(purchaseId: String) {
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(purchasableItemIds).setType(BillingClient.SkuType.INAPP)
        billingClient.querySkuDetailsAsync(params.build()) { result, skuDetails ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK && !skuDetails.isNullOrEmpty()) {
                purchasedItem = supportInfo?.supportItems?.firstOrNull { it.googleKey == purchaseId }
                if (purchasedItem == null) {
                    Toast.makeText(this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    return@querySkuDetailsAsync
                }
                val purchaseItem = skuDetails.first { it.sku == purchaseId }
                val flowParams =
                        BillingFlowParams.newBuilder().setSkuDetails(purchaseItem).build()
                billingClient.launchBillingFlow(this, flowParams)
                Log.e("mybury", " skuDetails[listNumber] : ${purchaseItem.title}")
            } else {
                Log.e("mybury", "No sku found from query")
            }
        }
    }

    /**
     * 구매 방식에 대해 처리한다. item 의 id 에 따라서
     * purchaseAlways 또는 purchaseOnce 로 보낸다.
     * (바텀에 결제 화면이 뜬 시점)
     */
    override fun onPurchasesUpdated(
            billingResult: BillingResult,
            purchaseList: MutableList<Purchase>?
    ) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchaseList?.forEach {
                    purchaseAlways(it.purchaseToken)
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.d("mybury", "You've cancelled the Google play billing process...")
            }
            else -> {
                Log.e("mybury", "Item not found or Google play billing error... : ${billingResult.responseCode}"
                )
            }
        }
    }

    // 최근 구매한 아이템을 알고자 할 때 사용
    override fun onPurchaseHistoryResponse(
            billingResult: BillingResult,
            purchaseHistoryList: MutableList<PurchaseHistoryRecord>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            if (!purchaseHistoryList.isNullOrEmpty()) {
                // 가장 최근에 구매된 아이템을 확인할 수 있다.
                purchaseHistoryList.forEach {
                    Log.d("mybury", "Previous Purchase Item : ${it.originalJson}")
                }
            }
        }
    }

    // 소비성 (계속 구매 가능한) 제품 구매시
    private fun purchaseAlways(purchaseToken: String) {
        val consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchaseToken).build()

        billingClient.consumeAsync(consumeParams) { billingResult, _ ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                purchasedItem?.let {
                    supportViewModel.purchasedItem(it.id, {
                        purchaseSuccess(it.itemPrice)
                    }, {
                        // 실패하면 어쩌지
                        purchaseFail()
                    })
                }

            } else {
                Log.e("mybury", "FAIL : ${billingResult.responseCode}")
            }
        }
    }

    // 일회성 제품 구매시
    private fun purchaseOnce(purchaseToken: String) {
        val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build()
        billingClient.acknowledgePurchase(params) { billingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                //  "구매가 성공했습니다!".showToast()
            } else {
                Log.e("TAG", "FAIL : ${billingResult.responseCode}")
            }
        }
    }

    companion object {
        private const val SUPPORT_PRICE = 50000
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