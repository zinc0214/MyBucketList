package womenproject.com.mybury.presentation.main.support

import android.app.ActionBar
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.DialogSupportSuccessBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment

class SupportSuccessDialogFragment() : BaseDialogFragment<DialogSupportSuccessBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.dialog_support_success

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
