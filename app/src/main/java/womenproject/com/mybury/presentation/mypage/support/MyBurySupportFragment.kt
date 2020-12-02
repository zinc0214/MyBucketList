package womenproject.com.mybury.presentation.mypage.support

import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.data.SupportInfo
import womenproject.com.mybury.databinding.FragmentMyburySupportBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.viewmodels.MyBurySupportViewModel

class MyBurySupportFragment : BaseFragment<FragmentMyburySupportBinding, MyBurySupportViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_mybury_support

    override val viewModel: MyBurySupportViewModel
        get() = MyBurySupportViewModel()

    private lateinit var purchaseItemListAdapter: PurchaseItemListAdapter

    override fun initDataBinding() {
        viewDataBinding.topLayout.title = "마이버리 후원하기"
        val desc: String = requireContext().getString(R.string.mybury_support_desc)
        viewDataBinding.supportDesc.text = Html.fromHtml(String.format(desc))
        viewDataBinding.supportPrice = ""

        getSupportInfo().apply {
            if (this == null) {
                ShowClosePopup { onBackPressedFragment() }.show(requireActivity().supportFragmentManager, "TAG")
            } else {
                setUpViews(this)
            }
        }
    }

    private fun setUpViews(supportInfo: SupportInfo) {
        viewDataBinding.apply {
            purchaseItemListView.layoutManager = GridLayoutManager(context, 2)
            purchaseItemListAdapter = PurchaseItemListAdapter(supportInfo.supportItems)
            purchaseItemListView.adapter = purchaseItemListAdapter

            supportPrice = supportInfo.totalPrice

            supportOnClickListener = View.OnClickListener {
                purchaseItemListAdapter.selectedItemNum?.let { item ->
                    purchaseSelectItem(item.googleKey, {
                        val updatePrice = viewDataBinding.supportPrice.toString().toInt() + item.itemPrice.toInt()
                        viewDataBinding.supportPrice = updatePrice.toString()
                        setCurrentSupportPrice(updatePrice)
                        "구매에 성공했습니다.".showToast()
                    }, {
                        "구매에 실패했습니다.".showToast()
                    })
                }
            }

            backBtnOnClickListener = View.OnClickListener {
                onBackPressedFragment()
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