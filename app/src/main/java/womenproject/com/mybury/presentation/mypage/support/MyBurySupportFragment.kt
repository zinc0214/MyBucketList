package womenproject.com.mybury.presentation.mypage.support

import android.util.Log
import com.android.billingclient.api.*
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.FragmentMyburySupportBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel

class MyBurySupportFragment : BaseFragment<FragmentMyburySupportBinding, BucketInfoViewModel>(), PurchasesUpdatedListener, PurchaseHistoryResponseListener {

    private lateinit var billingClient: BillingClient
    private var productIdsList: List<String>? = null

    override val layoutResourceId: Int
        get() = R.layout.fragment_mybury_support

    override val viewModel: BucketInfoViewModel
        get() = BucketInfoViewModel()

    override fun initDataBinding() {
        initBilling()
    }

    private fun initBilling() {
        billingClient = BillingClient.newBuilder(requireContext()).enablePendingPurchases().setListener(this)
                .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    loadProducts()
                    queryInventoryAsync()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.e("ayhan", "Service Disconnected...")
            }
        })
    }

    private fun loadProducts() {
        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, this)
    }

    private fun queryInventoryAsync() {
        if (productIdsList == null) return

        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(productIdsList!!).setType(BillingClient.SkuType.INAPP)
        billingClient.querySkuDetailsAsync(
                params.build()
        ) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.e("ayhan", "IsGoing : ${billingResult.responseCode}")
                if (!skuDetailsList.isNullOrEmpty()) {
                    for (item in skuDetailsList.withIndex()) {
                        val skuItem = item.value
                    }
                }
            }
            Log.e("ayhan", "IsNotGoing : ${billingResult.responseCode}")
        }
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
                Log.e("ayhan", "Item not found or Google play billing error...")
            }
        }
    }

    private fun acknowledgePurchase(purchaseToken: String) {
        val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build()
        billingClient.acknowledgePurchase(params) { billingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val debugMessage = billingResult.debugMessage
                Log.e("ayhan", "Item Purchased : $debugMessage")
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