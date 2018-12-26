package womenproject.com.mybury.view

import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import womenproject.com.mybury.R

/**
 * Created by HanAYeon on 2018. 12. 4..
 */

open class BaseDialogFragment : DialogFragment() {


    private lateinit var mainMsg: String

    companion object {

        const val DIALOG_MSG = "dialog_msg"

        fun Instance(mainMsg: String): BaseDialogFragment {
            val bundle = Bundle()
            bundle.putString(DIALOG_MSG, mainMsg)

            val fragment = BaseDialogFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mainMsg = arguments!!.getString(DIALOG_MSG)
        }
    }

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialog_fragment_width)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_message, null)

        builder.setView(view)

        val dialog = builder.create()
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)

        return dialog

    }

    private fun dialogDismiss() {
        this.dismiss()
    }
}