package womenproject.com.mybury.presentation.main

import android.app.ActionBar
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.SupportSuccessDialogBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment

class SupportSuccessDialogFragment() : BaseDialogFragment<SupportSuccessDialogBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.support_success_dialog

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialogFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
        dialog?.window?.setDimAmount(0.8F)

    }

    override fun initDataBinding() {

    }

}
