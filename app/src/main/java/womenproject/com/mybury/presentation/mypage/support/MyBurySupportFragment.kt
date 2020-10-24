package womenproject.com.mybury.presentation.mypage.support

import android.util.Log
import com.android.billingclient.api.*
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.FragmentMyburySupportBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel

class MyBurySupportFragment : BaseFragment<FragmentMyburySupportBinding, BucketInfoViewModel>(),
        PurchasesUpdatedListener,
        PurchaseHistoryResponseListener {

    private lateinit var billingClient: BillingClient
    val skuListToQuery = ArrayList<String>()

    override val layoutResourceId: Int
        get() = R.layout.fragment_mybury_support

    override val viewModel: BucketInfoViewModel
        get() = BucketInfoViewModel()

    override fun initDataBinding() {
        initBilling()

    }

    // BillingClient 초기화
    private fun initBilling() {

        // Google PlayConsole 의 상품Id 와 동일하게 적어준다
        skuListToQuery.add("ice_cream")
        skuListToQuery.add("chicken")

        billingClient = BillingClient.newBuilder(requireContext()).enablePendingPurchases().setListener(this)
                .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                   // queryPurchases()
                    setUpView()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.e("ayhan", "Service Disconnected...")
            }
        })
    }

    // BillingSetUp 이 완료되었을 때 필요한 할 일 설정
    fun queryPurchases() {
        // BillingClient 의 준비가 되지않은 상태라면 돌려보낸다
        if (!billingClient.isReady) {
            Log.e("ayhan", "queryPurchases: BillingClient is not ready")
        }

        // InApp 결제한 적이 있는 아이템으 ㅣ정보를  확인한다
        /* val result = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
         if (result.purchasesList == null) {
             Log.e("ayhan", "No existing in app purchases found.")
         } else {
             Log.e("ayhan", "Existing purchases: ${result.purchasesList}")
         }*/
    }

    private fun setUpView() {
        viewDataBinding.iceCreamPurchase.setOnClickListener { purchaseItem(1) }
        viewDataBinding.chickenPurchase.setOnClickListener { purchaseItem(0) }
    }

    private fun purchaseItem(listNumber: Int) {
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuListToQuery).setType(BillingClient.SkuType.INAPP)
        billingClient.querySkuDetailsAsync(params.build()
        ) { result, skuDetails ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK && skuDetails != null) {
                Log.e("ayhan", "querySkuDetailsAsync : $skuDetails,,, ${skuDetails[listNumber].title}")
                purchaseGoods(skuDetails[listNumber])
            } else {
                Log.e("ayhan", "No sku found from query")
            }
        }
    }

    private fun purchaseGoods(skuDetail: SkuDetails) {
        val parms = BillingFlowParams.newBuilder().setSkuDetails(skuDetail).build()
        val responseCode = billingClient.launchBillingFlow(requireActivity(), parms)
        Log.e("ayhan", "purchaes : ${responseCode.responseCode}, ${responseCode.debugMessage}")
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchaseList: MutableList<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                for (purchase in purchaseList!!) {
                    acknowledgePurchase(purchase.purchaseToken)
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.e("ayhan", "You've cancelled the Google play billing process...")
            }
            else -> {
                Log.e("ayhan", "Item not found or Google play billing error... : ${billingResult.responseCode}")
            }
        }
    }

    private fun acknowledgePurchase(purchaseToken: String) {
        val consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchaseToken).build()

        billingClient.consumeAsync(consumeParams) { billingResult, st ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.e("ayhan", "Item is Go!! , ${billingResult.debugMessage} , ${billingResult.responseCode}, ${st}")
            } else {
                Log.e("ayhan", "Item is Go!! , ${billingResult.debugMessage}, ${billingResult.responseCode}, ${st}")
            }
        }
    }

    override fun onPurchaseHistoryResponse(billingResult: BillingResult, purchaseHistoryList: MutableList<PurchaseHistoryRecord>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            if (!purchaseHistoryList.isNullOrEmpty()) {
                for (purchase in purchaseHistoryList) {
                    // 구매된 목록 확인
                    Log.e("ayhan", "is Alreay Purches = $purchase")
                }
            }
        }

    }
}