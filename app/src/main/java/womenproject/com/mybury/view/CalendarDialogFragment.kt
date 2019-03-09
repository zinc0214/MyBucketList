package womenproject.com.mybury.view

import android.annotation.SuppressLint
import android.app.ActionBar
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.CalendarDialogBinding

/**
 * Created by HanAYeon on 2019. 3. 8..
 */

@SuppressLint("ValidFragment")
class CalendarDialogFragment(private var ddaySetListener: (String) -> Unit) : DialogFragment() {


    private lateinit var mainMsg: String
    private lateinit var dday : String

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

        val binding = DataBindingUtil.inflate<CalendarDialogBinding>(
                inflater, R.layout.calendar_dialog, container, false).apply {

            calendarView.setOnDateChangedListener {
                widget, date, selected ->
                Toast.makeText(context!!.getApplicationContext(), date.year.toString() + "-" + date.month + "-" + date.day + "", Toast.LENGTH_SHORT).show()
                if(date.month+1 < 10) {
                    dday = "${date.year}/0${date.month+1}/${date.day}"
                } else {
                    dday = "${date.year}/${date.month+1}/${date.day}"
                }

            }

            confirmButtonClickListener = confirmOnClickListener()
            cancelButtonClickListener = cancelOnClickListener()
        }


        dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCanceledOnTouchOutside(true)

        return binding.root
    }



    private fun confirmOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            ddaySetListener.invoke(dday)
            this.dismiss()
        }
    }


    private fun cancelOnClickListener() : View.OnClickListener {
        return View.OnClickListener {
            this.dismiss()
        }
    }

    protected fun setDialogMessage(dialogMessage: String) {
        mainMsg = dialogMessage
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, "Tag")
    }

}