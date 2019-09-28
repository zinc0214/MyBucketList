package womenproject.com.mybury.presentation.main

import android.app.ActionBar
import android.view.View
import womenproject.com.mybury.R
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import womenproject.com.mybury.databinding.MainFilterDialogBinding
import womenproject.com.mybury.presentation.viewmodels.FilterDialogViewModel

/**
 * Created by HanAYeon on 2019. 1. 15..
 */


open class FilterDialogFragment : BaseDialogFragment<MainFilterDialogBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.main_filter_dialog

    override fun initDataBinding() {
        viewDataBinding.viewModel = FilterDialogViewModel()
        viewDataBinding.filterSetClickListener = createOnClickFilterSetListener()
    }

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialogFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }




    private fun createOnClickFilterSetListener() : View.OnClickListener {
        return View.OnClickListener {
            dialogDismiss()
        }
    }

    private fun dialogDismiss() {
        this.dismiss()
    }

}