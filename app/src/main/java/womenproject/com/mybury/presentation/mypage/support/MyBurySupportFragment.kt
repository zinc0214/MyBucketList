package womenproject.com.mybury.presentation.mypage.support

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
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
    private var isCurrentSupporting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val goToActionCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isCurrentSupporting) {
                    // 결제가 끝나지 않는 상태에서는 ignore back button
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, goToActionCallback)
    }

    override fun initDataBinding() {
        viewDataBinding.supportPrice = ""

        getSupportInfo().apply {
            if (this == null) {
                ShowClosePopup { onBackPressedFragment() }.show(
                    requireActivity().supportFragmentManager,
                    "TAG"
                )
            } else {
                setUpViews(this)
            }
        }
    }

    private fun setUpViews(supportInfo: SupportInfo) {
        viewDataBinding.apply {
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.justifyContent = JustifyContent.SPACE_EVENLY
            layoutManager.flexWrap = FlexWrap.WRAP
            purchaseItemListView.layoutManager = layoutManager

            purchaseItemListAdapter =
                PurchaseItemListAdapter(supportInfo.supportItems.filter { it.dpYn == "Y" })
            purchaseItemListView.adapter = purchaseItemListAdapter

            supportPrice = supportInfo.totalPrice

            supportOnClickListener = View.OnClickListener {
                isCurrentSupporting = true
                purchaseItemListAdapter.selectedItemNum?.let { item ->
                    purchaseSelectItem(item.googleKey, {
                        val updatePrice = supportPrice.toString().toInt() + item.itemPrice.toInt()
                        supportPrice = updatePrice.toString()
                        setCurrentSupportPrice(updatePrice)
                        isCurrentSupporting = false
                    }, {
                        isCurrentSupporting = false
                    })
                }
            }

            backBtnOnClickListener = View.OnClickListener {
                onBackPressedFragment()
            }

            collapsibleToolbar.setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionTrigger(
                    p0: MotionLayout?,
                    p1: Int,
                    p2: Boolean,
                    p3: Float
                ) {
                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

                }

                override fun onTransitionChange(p0: MotionLayout, p1: Int, p2: Int, p3: Float) {
                    if (p0.targetPosition >= 0.3F) {
                        titleMotionLayout.transitionToEnd()
                    } else {
                        titleMotionLayout.transitionToStart()
                    }

                }

                override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {

                }

            })
        }
    }
}

class ShowClosePopup(private val back: () -> Unit) : BaseNormalDialogFragment() {

    init {
        TITLE_MSG = "아이템 로드 실패"
        CONTENT_MSG = "후원가능한 아이템을 \n가져오지 못했습니다."
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