@file:Suppress("INTEGER_OVERFLOW")

package womenproject.com.mybury.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.PurchaseHistoryResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.BuildConfig
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.Preference.Companion.isAlreadySupportShow
import womenproject.com.mybury.data.PurchasableItem
import womenproject.com.mybury.data.SupportInfo
import womenproject.com.mybury.databinding.ActivityMainBinding
import womenproject.com.mybury.presentation.base.BaseActiviy
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.dialog.NetworkFailDialog
import womenproject.com.mybury.presentation.main.MainFragment
import womenproject.com.mybury.presentation.main.MainFragmentDirections
import womenproject.com.mybury.presentation.main.support.SupportDialogFragment
import womenproject.com.mybury.presentation.main.support.SupportFailDialogFragment
import womenproject.com.mybury.presentation.main.support.SupportSuccessDialogFragment
import womenproject.com.mybury.presentation.mypage.MyPageFragmentDirections
import womenproject.com.mybury.presentation.viewmodels.MyBurySupportViewModel
import womenproject.com.mybury.util.ScreenUtils.Companion.setStatusBar
import womenproject.com.mybury.util.showToast
import java.util.Date


/**
 * Created by HanAYeon on 2018. 11. 26..
 */

@AndroidEntryPoint
class MainActivity : BaseActiviy(), PurchasesUpdatedListener, PurchaseHistoryResponseListener {

    private lateinit var binding: ActivityMainBinding
    private val supportViewModel by viewModels<MyBurySupportViewModel>()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private var mInterstitialAd: InterstitialAd? = null
    private var isAdShow = true

    private lateinit var billingClient: BillingClient
    private var productDetailsList = mutableListOf<ProductDetails>()
    private var purchasedItem: PurchasableItem? = null
    private var previousToken = ""

    var supportInfo: SupportInfo? = null
    private lateinit var purchaseSuccess: () -> Unit
    private lateinit var purchaseFail: () -> Unit

    private var acknowledgePurchaseItemIsNullList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initAdmob()
        drawerLayout = binding.drawerLayout
        navController = Navigation.findNavController(this, R.id.nav_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        val baseViewModel = BaseViewModel()

        if (baseViewModel.isNetworkDisconnect()) {
            NetworkFailDialog().show(supportFragmentManager, "tag")
        }

        supportViewModel.getPurchasableItem()

        supportViewModel.supportInfo.observe(this) { info ->
            setAdShowable(info.totalPrice.toInt())
            initBillingClient(info.supportItems)
            supportInfo = info
            supportInfo?.supportItems?.forEach {
                it.isPurchasable = true
            }
            stopLoading()
        }

        supportViewModel.supportPrice.observe(this) { price ->
            supportInfo?.totalPrice = price
        }

        purchaseFail = {
            // DO NoTHING
        }

        purchaseSuccess = {
            // DO NoTHING
        }

        Preference.setEnableShowAlarm(this, true)
        setStatusBar(this, R.color._ffffff)

        val animation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        binding.loadingLayout.loadingImg.animation = animation
    }

    private fun initAdmob() {
        MobileAds.initialize(this)
        loadAd()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, AD_UNIT_ID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d("myBury", "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("myBury", "Ad Load Fail : ${adError.message}, ${adError}")
                    mInterstitialAd = null
                }
            })
    }

    // Show the ad if it's ready. Otherwise toast and restart the game.
    private fun showInterstitial() {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("myBury", "Ad was dismissed.")
                val currentTime = Date().time
                val daysOverTime: Long = 1000 * 60 * 60 * 24 * 30 // 1달로 설정
                val shownTime = isAlreadySupportShow(this@MainActivity)
                val isOverTime = currentTime - shownTime > daysOverTime || shownTime.equals(0f)
                if (isOverTime || BuildConfig.DEBUG) {
                    showSupportDialogFragment()
                }
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.d("myBury", "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("mybury", "Ad showed fullscreen content.")
                mInterstitialAd = null
            }
        }
    }

    fun showAds() {
        Log.e("myBury", "isAdShow : $isAdShow")
        showInterstitial()
        if (isAdShow) {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
                if (BuildConfig.DEBUG) {
                    showSupportDialogFragment()
                }
            }
        }
    }


    fun startLoading() {
        if (binding.loadingLayout.layout.visibility == View.GONE) {
            binding.loadingLayout.layout.visibility = View.VISIBLE
            binding.drawerLayout.isClickable = false
        }
    }

    fun stopLoading() {
        if (binding.loadingLayout.layout.visibility == View.VISIBLE) {
            binding.loadingLayout.layout.visibility = View.GONE
            binding.drawerLayout.isClickable = true
        }

    }


    fun setAdShowable(price: Int) {
        isAdShow = price < SUPPORT_PRICE || BuildConfig.DEBUG
    }

    fun purchaseSelectItem(id: String, purchaseSuccess: () -> Unit, purchaseFail: () -> Unit) {
        purchaseItem(id)
        this.purchaseSuccess = purchaseSuccess
        this.purchaseFail = purchaseFail
    }

    private fun showSupportDialogFragment() {
        val fragment = SupportDialogFragment()
        fragment.setButtonAction({
            purchaseItem("cheer.3300")
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
        billingClient =
            BillingClient.newBuilder(this).setListener(this).enablePendingPurchases().build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProductDetails(items)
                    getAllPurchasedItem()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.e("mybury", "Service Disconnected.")
            }
        })
    }

    /**
     * 구매 가능 목록 호출
     */
    fun queryProductDetails(items: List<PurchasableItem>) {
        val purchaseItems = items.filter { it.isPurchasable }

        val productList: ArrayList<QueryProductDetailsParams.Product> = ArrayList()
        purchaseItems.forEach { item ->
            productList.add(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(item.googleKey)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            )
        }
        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, mutableList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.e("ayhan", "queryProductDetailsAsync : $mutableList")
                if (mutableList.isEmpty()) {
                    "다시 시도해주세요.".showToast(this)
                } else {
                    productDetailsList.addAll(mutableList)
                }
            } else {
                "다시 시도해주세요.".showToast(this)
            }

        }
    }


    /**
     * 최근에 결제한 아이템을 확인하는 목적
     * checkPurchaseHistory function 과 다르게  consumeAsync 를 통해 구매된 상품도 확인할 수 있다.
     * !!주의!! 아이템 Id 로만 가져오기 때문에 여러번 구매하더라도 가장 최근의 제품만 가져온다.
     */
    fun getAllPurchasedItem() {
        val params = QueryPurchaseHistoryParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
        billingClient.queryPurchaseHistoryAsync(params.build(), this)
    }

    /**
     * 아이템을 구매하기 위해서는 구매가능한 아이템 리스트와 확인이 필요하다.
     * 리스트가 존재할 경우 실제 구매를 할 수 있다.
     */
    private fun purchaseItem(purchaseId: String) {
        startLoading()
        val productDetails = productDetailsList.find { it.productId == purchaseId }
        if (productDetails == null) {
            Toast.makeText(this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()


        // Launch the billing flow
        val billingResult = billingClient.launchBillingFlow(this, billingFlowParams)
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            purchasedItem =
                supportInfo?.supportItems?.firstOrNull { it.googleKey == purchaseId }
            if (purchasedItem == null) {
                "다시 시도해주세요.".showToast(this)
                stopLoading()
                return
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
        if (purchaseList == null) {
            purchaseFail.invoke()
        }
        startLoading()
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchaseList?.forEach {
                    handlePurchase(it)
                }
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.d("mybury", "You've cancelled the Google play billing process...")
                "결제가 취소되었습니다".showToast(this)
                purchaseFail.invoke()
                stopLoading()
            }

            else -> {
                "다시 시도해주세요.".showToast(this)
                purchaseFail.invoke()
                //  stopLoading()
            }
        }
    }

    // 최근 구매한 아이템을 알고자 할 때 사용
    override fun onPurchaseHistoryResponse(
        billingResult: BillingResult,
        purchaseHistoryList: MutableList<PurchaseHistoryRecord>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

            val recentSupport = supportInfo?.recentSupport ?: return

            // 가장 최근에 구매된 아이템을 확인할 수 있다.
            purchaseHistoryList?.forEach { history ->
                //만약 여기의 토큰값이 서버에 있는 "실패토큰" 목록에 있지만, getAcknowledgePurchasedItem 목록에는 없다면 성공한 것으로 간주한다.
                recentSupport.forEach { item ->
                    acknowledgePurchaseItemIsNullList.firstOrNull { it == item.token }?.let {
                        if (history.purchaseToken == item.token && item.susYn == "N") {
                            supportViewModel.editSuccessItem(item.token, "Y", {
                                "누락된 후원 정보가 갱신되었습니다.".showToast(this)
                                supportViewModel.updateSupportPrice()
                            }, {
                                // error
                            })
                        }
                    }
                }
            }
        }
    }

    // 소비성 (계속 구매 가능한) 제품 구매시
    private fun handlePurchase(purchase: Purchase) {
        val purchaseToken = purchase.purchaseToken
        val consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchaseToken).build()
        billingClient.consumeAsync(consumeParams) { billingResult, _ ->
            if (previousToken == purchaseToken && billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                purchaseFail.invoke()
            } else {
                previousToken = purchaseToken
                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        if (purchasedItem != null) {
                            supportViewModel.purchasedItem(purchasedItem?.id!!, purchaseToken, "Y",
                                {
                                    // if success
                                    showSupportPurchaseSuccessDialog()
                                    purchaseSuccess.invoke()
                                    supportViewModel.getPurchasableItem()
                                },
                                {
                                    // if fail
                                    showSupportPurchaseFailDialog(
                                        purchaseToken,
                                        billingResult.responseCode.toString()
                                    )
                                    purchaseFail.invoke()
                                })
                        } else {
                            purchaseFail.invoke()
                        }
                    }

                    else -> {
                        purchasedItem?.let {
                            supportViewModel.purchasedItem(it.id, purchaseToken, "N", {
                                showSupportPurchaseFailDialog(
                                    purchaseToken,
                                    billingResult.responseCode.toString()
                                )
                                stopLoading()
                                purchaseFail.invoke()
                            }, {
                                showSupportPurchaseFailDialog(
                                    purchaseToken,
                                    billingResult.responseCode.toString()
                                )
                                stopLoading()
                                purchaseFail.invoke()
                            })
                        }
                    }
                }
            }
        }
    }

    private fun showSupportPurchaseSuccessDialog() {
        val supportSuccessDialogFragment = SupportSuccessDialogFragment()
        supportSuccessDialogFragment.show(supportFragmentManager, "tag")
    }

    private fun showSupportPurchaseFailDialog(token: String, errorCode: String) {
        val supportFailDialogFragment = SupportFailDialogFragment(token, errorCode)
        supportFailDialogFragment.show(supportFragmentManager, "tag")
    }

    companion object {
        private const val SUPPORT_PRICE = 3300
    }
}

val AD_UNIT_ID =
    if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1033173712" else "ca-app-pub-6302671173915322/9547430142"
