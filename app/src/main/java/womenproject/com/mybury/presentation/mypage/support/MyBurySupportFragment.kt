package womenproject.com.mybury.presentation.mypage.support

import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.android.billingclient.api.*
import womenproject.com.mybury.R
import womenproject.com.mybury.data.PurchasableItem
import womenproject.com.mybury.data.SupportInfo
import womenproject.com.mybury.databinding.FragmentMyburySupportBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.viewmodels.MyBurySupportViewModel

class MyBurySupportFragment : BaseFragment<FragmentMyburySupportBinding, MyBurySupportViewModel>(),
        PurchasesUpdatedListener,
        PurchaseHistoryResponseListener {

    override val layoutResourceId: Int
        get() = R.layout.fragment_mybury_support

    override val viewModel: MyBurySupportViewModel
        get() = MyBurySupportViewModel()

    private lateinit var billingClient: BillingClient
    private lateinit var purchaseItemListAdapter: PurchaseItemListAdapter
    private val purchasableItemIds = ArrayList<String>()
    private val purchasedItemIds = ArrayList<String>()
    private lateinit var supportInfo: SupportInfo

    private var purchasedItem: PurchasableItem? = null

    override fun initDataBinding() {
        val myBurySupportViewModel = ViewModelProvider(this).get(MyBurySupportViewModel::class.java)

        myBurySupportViewModel.getPurchasableItem {
            ShowClosePopup { onBackPressedFragment() }.show(requireActivity().supportFragmentManager, "TAG")
        }
        myBurySupportViewModel.supportInfo.observe(viewLifecycleOwner, Observer { info ->
            supportInfo = info
            supportInfo.supportItems.forEach {
                it.isPurchasable = true
            }
            initBillingClient(info.supportItems)
        })

        viewDataBinding.topLayout.title = "마이버리 후원하기"
        val desc: String = requireContext().getString(R.string.mybury_support_desc)
        viewDataBinding.supportDesc.text = Html.fromHtml(String.format(desc))
        viewDataBinding.supportPrice = ""
    }

    private fun setUpViews(supportInfo: SupportInfo) {
        viewDataBinding.apply {
            purchaseItemListView.layoutManager = GridLayoutManager(context, 2)
            purchaseItemListAdapter = PurchaseItemListAdapter(supportInfo.supportItems)
            purchaseItemListView.adapter = purchaseItemListAdapter

            supportPrice = supportInfo.totalPrice ?: ""
            supportOnClickListener = View.OnClickListener {
                purchaseItemListAdapter.selectedItemNum?.let {
                    purchaseItem(it.googleKey)
                }
            }

            backBtnOnClickListener = View.OnClickListener {
                onBackPressedFragment()
            }
        }
    }

    /**
     *  BillingClient 초기화
     */
    private fun initBillingClient(items: List<PurchasableItem>) {
        billingClient = BillingClient.newBuilder(requireContext()).enablePendingPurchases().setListener(this)
                .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    getAcknowledgePurchasedItem()
                    getAllPurchasedItem()
                    setPurchasableList(items)
                    setUpViews(supportInfo)
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
            supportInfo.supportItems.filter { it.googleKey == purchasedId }.forEach {
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
                purchasedItem = supportInfo.supportItems.firstOrNull { it.googleKey == purchaseId }
                val purchaseItem = skuDetails.first { it.sku == purchaseId }
                val flowParams =
                        BillingFlowParams.newBuilder().setSkuDetails(purchaseItem).build()
                billingClient.launchBillingFlow(requireActivity(), flowParams)
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
                    viewModel.purchasedItem(it.id, {
                        "구매가 성공했습니다!".showToast()
                        val updatePrice = supportInfo.totalPrice.toInt() + it.itemPrice.toInt()
                        viewDataBinding.supportPrice = updatePrice.toString()
                        setCurrentSupportPrice(updatePrice)
                    }, {
                        // 실패하면 어쩌지
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
                "구매가 성공했습니다!".showToast()
            } else {
                Log.e("TAG", "FAIL : ${billingResult.responseCode}")
            }
        }
    }

    private fun String.showToast() {
        Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT).show()
    }
}

class ShowClosePopup(private val back: () -> Unit) : BaseNormalDialogFragment() {

    init {
        TITLE_MSG = "아무튼 안됨"
        CONTENT_MSG = "후원가능한 아이템로드에 실패했습니다."
        CANCEL_BUTTON_VISIBLE = false
        GRADIENT_BUTTON_VISIBLE = true
        CONFIRM_TEXT = "확인"
        CANCEL_ABLE = false
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            dismiss()
            back.invoke()
        }
    }
}