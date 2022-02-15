package womenproject.com.mybury.presentation.main

import android.app.ActionBar
import android.text.Html
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.DialogFragmentWarningBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment

@AndroidEntryPoint
class WarningDialogFragment(private val confirmClick: () -> Unit) : BaseDialogFragment<DialogFragmentWarningBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.dialog_fragment_warning

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialogFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
        dialog?.window?.setDimAmount(0.8F)
        dialog?.setCancelable(false)

    }

    override fun initDataBinding() {
        viewDataBinding.apply {
            title = "마이버리 점검중"
            timeIsVisible = false

            val desc: String = requireContext().getString(R.string.warning_desc)
            descTextView.text = Html.fromHtml(String.format(desc))

            confirmClickListener = View.OnClickListener {
                dismiss()
                confirmClick.invoke()
            }
        }
    }

}
