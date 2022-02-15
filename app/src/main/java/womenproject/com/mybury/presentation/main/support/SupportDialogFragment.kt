package womenproject.com.mybury.presentation.main.support

import android.app.ActionBar
import android.text.Html
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference.Companion.setAlreadySupportShow
import womenproject.com.mybury.databinding.DialogSupportOnceBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import java.util.*

@AndroidEntryPoint
class SupportDialogFragment : BaseDialogFragment<DialogSupportOnceBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.dialog_support_once

    private lateinit var goToPurchase: () -> Unit
    private lateinit var goToDetail: () -> Unit

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialogFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    fun setButtonAction(goToPurchase: () -> Unit, goToDetail: () -> Unit) {
        this.goToPurchase = goToPurchase
        this.goToDetail = goToDetail
    }

    override fun initDataBinding() {
        viewDataBinding.apply {
            button.setOnClickListener { goToPurchase() }
            detailButton.setOnClickListener { goToDetail() }
            supportNotShowButton.setOnClickListener {
                val time = Date().time
                setAlreadySupportShow(requireContext(), time)
                dismiss()
            }

            val desc: String = requireContext().getString(R.string.mybury_dialog_desc)
            viewDataBinding.subDesc.text = Html.fromHtml(String.format(desc))
        }
    }
}