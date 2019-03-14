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
import womenproject.com.mybury.base.BaseDialogFragment
import womenproject.com.mybury.databinding.CalendarDialogBinding
import java.util.*


/**
 * Created by HanAYeon on 2019. 3. 8..
 */

@SuppressLint("ValidFragment")
class CalendarDialogFragment(private var ddaySetListener: (String) -> Unit) : BaseDialogFragment<CalendarDialogBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.calendar_dialog

    private var dday = ""

    override fun initStartView() {

        val currentTime = Calendar.getInstance().getTime()
        viewDataBinding.calendarView.setCurrentDate(currentTime)
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

        viewDataBinding.calendarView.setOnDateChangedListener { widget, date, selected ->
            var month = "1"
            var day = "1"

            month = if (date.month + 1 < 10) {
                "0${date.month + 1}"
            } else {
                date.month.toString()
            }


            day = if (date.day < 10) {
                "0${date.day}"
            } else {
                date.day.toString()
            }

            dday = "${date.year}/$month/$day"


        }

        viewDataBinding.bottomSheet.confirmButtonClickListener = confirmOnClickListener()
        viewDataBinding.bottomSheet.cancelButtonClickListener = cancelOnClickListener()
    }


    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialogFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    private fun confirmOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            if(!dday.isEmpty()) {
                ddaySetListener.invoke(dday)
            }
            this.dismiss()
        }
    }

    private fun cancelOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            this.dismiss()
        }
    }

}