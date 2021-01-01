package womenproject.com.mybury.presentation.main

import android.app.ActionBar
import android.text.Html
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference.Companion.setAlreadySupportShow
import womenproject.com.mybury.databinding.FragmentSupportDialogBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment

class SupportDialogFragment : BaseDialogFragment<FragmentSupportDialogBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_support_dialog


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

            val desc: String = requireContext().getString(R.string.mybury_dialog_desc)
            viewDataBinding.subDesc.text = Html.fromHtml(String.format(desc))

            setAlreadySupportShow(requireContext(), true)
        }
    }
}