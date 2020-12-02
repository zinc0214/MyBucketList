package womenproject.com.mybury.presentation.main

import android.app.ActionBar
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.FragmentSupportDialogBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment

class SupportDialogFragment(private val goToPurchase: () -> Unit, private val goToDetail: () -> Unit) : BaseDialogFragment<FragmentSupportDialogBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.main_filter_dialog

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialogFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    override fun initDataBinding() {
        viewDataBinding.apply {
            button.setOnClickListener { goToPurchase() }
            detailButton.setOnClickListener { goToDetail() }
        }
    }
}