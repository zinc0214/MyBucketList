package womenproject.com.mybury.view

import android.app.ActionBar
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import womenproject.com.mybury.R
import womenproject.com.mybury.base.BaseDialogFragment
import womenproject.com.mybury.databinding.MainFilterDialogBinding
import womenproject.com.mybury.viewmodels.FilterDialogViewModel

/**
 * Created by HanAYeon on 2019. 1. 15..
 */


open class FilterDialogFragment : BaseDialogFragment<MainFilterDialogBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.main_filter_dialog

    override fun initStartView() {
        viewDataBinding.viewModel = FilterDialogViewModel()
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
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