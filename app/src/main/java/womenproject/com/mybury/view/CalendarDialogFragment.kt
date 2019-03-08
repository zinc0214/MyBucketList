package womenproject.com.mybury.view

import android.annotation.SuppressLint
import android.app.ActionBar
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.calendar_dialog.*
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.MainFilterDialogBinding
import womenproject.com.mybury.viewmodels.FilterDialogViewModel

/**
 * Created by HanAYeon on 2019. 3. 8..
 */

@SuppressLint("ValidFragment")
open class CalendarDialogFragment(private var ddaySetListener: (String) -> Unit) : DialogFragment() {


    private lateinit var mainMsg: String

    companion object {

        const val DIALOG_MSG = "dialog_msg"

        fun instance(ddaySetListener: (String) -> Unit): CalendarDialogFragment {
            val fragment = CalendarDialogFragment(ddaySetListener)

            return fragment
        }
    }

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialogFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<MainFilterDialogBinding>(
                inflater, R.layout.calendar_dialog, container, false).apply {
            viewModel = FilterDialogViewModel()

        }

        binding.apply {

        }

        dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCanceledOnTouchOutside(true)

        return binding.root
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