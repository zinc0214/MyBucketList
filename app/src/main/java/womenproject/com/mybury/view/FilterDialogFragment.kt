package womenproject.com.mybury.view

import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.MainFilterDialogBinding
import womenproject.com.mybury.viewmodels.FilterDialogViewModel

/**
 * Created by HanAYeon on 2019. 1. 15..
 */


open class FilterDialogFragment : DialogFragment() {


    private lateinit var mainMsg: String

    companion object {

        const val DIALOG_MSG = "dialog_msg"

        fun instance(): FilterDialogFragment {
            val fragment = FilterDialogFragment()

            return fragment
        }
    }

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialog_fragment_width)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<MainFilterDialogBinding>(
                inflater, R.layout.main_filter_dialog, container, false).apply {
            viewModel = FilterDialogViewModel()

        }

        binding.apply {
            filterSetClickListener = createOnClickFilterSetListener()
        }

        dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCanceledOnTouchOutside(true)

        return binding.root
    }


    private fun createOnClickFilterSetListener() : View.OnClickListener {
        return View.OnClickListener {
            dialogDismiss()
        }
    }

    private fun dialogDismiss() {
        this.dismiss()
    }

    protected fun setDialogMessage(dialogMessage: String) {
        mainMsg = dialogMessage
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, "Tag")
    }

}