package womenproject.com.mybury.presentation.base

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
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.DialogFragmentBaseBinding

/**
 * Created by HanAYeon on 2018. 12. 4..
 */

open class BaseNormalDialogFragment : DialogFragment() {


    private lateinit var mainMsg: String

    var TITLE_MSG = "title"
    var CONTENT_MSG = "content"
    var CANCEL_BUTTON_VISIBLE = true
    var GRADIENT_BUTTON_VISIBLE = true
    var CANCEL_TEXT = "취소"
    var CONFIRM_TEXT = "확인"
    var CANCEL_ABLE = true


    override fun onResume() {
        super.onResume()
        val dialogWidth = resources.getDimensionPixelSize(R.dimen.writeFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<DialogFragmentBaseBinding>(
                inflater, R.layout.dialog_fragment_base, container, false).apply {
            title = TITLE_MSG
            content = CONTENT_MSG
            cancelText.text = CANCEL_TEXT
            confirmText.text = CONFIRM_TEXT
            cancelText.visibility = if (CANCEL_BUTTON_VISIBLE) View.VISIBLE else View.GONE
            gradientLayout.visibility = if (GRADIENT_BUTTON_VISIBLE) View.VISIBLE else View.GONE
            baseLayout.visibility = if (GRADIENT_BUTTON_VISIBLE) View.GONE else View.VISIBLE
            binderImg.visibility = if (GRADIENT_BUTTON_VISIBLE) View.GONE else View.VISIBLE
            cancelButtonClickListener = createOnClickCancelListener()
            confirmButtonClickListener = createOnClickConfirmListener()
        }

        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCanceledOnTouchOutside(CANCEL_ABLE)
        dialog!!.setCancelable(CANCEL_ABLE)

        return binding.root
    }


    open fun createOnClickCancelListener() : View.OnClickListener {
        return View.OnClickListener {
            dialogDismiss()
        }
    }


    open fun createOnClickConfirmListener() : View.OnClickListener {
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