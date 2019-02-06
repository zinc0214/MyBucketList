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
import womenproject.com.mybury.databinding.FragmentBaseDialogBinding
import womenproject.com.mybury.viewmodels.BaseDialogViewModel

/**
 * Created by HanAYeon on 2018. 12. 4..
 */

open class BaseDialogFragment : DialogFragment() {


    private lateinit var mainMsg: String

    companion object {

        const val TITLE_MSG = "title"
        const val CONTENT_MSG = "content"
        const val BUTTON_VISIBLE = "visible"

        fun Instance(titleMsg: String, contentMsg:String, isCancelBtnVisible:Boolean): BaseDialogFragment {
            val bundle = Bundle()
            bundle.putString(TITLE_MSG, titleMsg)
            bundle.putString(CONTENT_MSG, contentMsg)
            bundle.putBoolean(BUTTON_VISIBLE, isCancelBtnVisible)

            val fragment = BaseDialogFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentBaseDialogBinding>(
                inflater, R.layout.fragment_base_dialog, container, false).apply {
            viewModel = BaseDialogViewModel()

        }

        binding.apply {
            title = arguments?.getString(TITLE_MSG)
            content = arguments?.getString(CONTENT_MSG)
            buttonVisible = if(arguments?.getBoolean(BUTTON_VISIBLE)!!) View.VISIBLE else View.GONE
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