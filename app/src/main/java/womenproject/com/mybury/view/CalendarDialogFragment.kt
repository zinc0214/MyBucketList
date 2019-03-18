package womenproject.com.mybury.view

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import com.prolificinteractive.materialcalendarview.CalendarDay
import womenproject.com.mybury.R
import womenproject.com.mybury.base.BaseDialogFragment
import womenproject.com.mybury.databinding.CalendarDialogBinding
import womenproject.com.mybury.ui.CurrentDateDecorator


/**
 * Created by HanAYeon on 2019. 3. 8..
 */

@SuppressLint("ValidFragment")
class CalendarDialogFragment(private var ddaySetListener: (String, CalendarDay) -> Unit, private var calendarDay: CalendarDay) : BaseDialogFragment<CalendarDialogBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.calendar_dialog

    private var dday = ""
    private var calendarVisible = true

    override fun initStartView() {

        viewDataBinding.calendarView.selectedDate = calendarDay
        viewDataBinding.calendarView.addDecorator(CurrentDateDecorator())
        viewDataBinding.calendarView.setDateTextAppearance(R.style.CalendarDate)
        viewDataBinding.calendarView.setWeekDayTextAppearance(R.style.CalendarWeek)
        viewDataBinding.calendarView.topbarVisible = false
        viewDataBinding.dateTitle.setSelectDate(calendarDay.year.toString(), calendarDay.month.toString())
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

        viewDataBinding.calendarView.setOnMonthChangedListener { widget, date ->
            viewDataBinding.dateTitle.setSelectDate(date.year.toString(), (date.month+1).toString())
        }

        viewDataBinding.bottomSheet.confirmButtonClickListener = confirmOnClickListener()
        viewDataBinding.bottomSheet.cancelButtonClickListener = cancelOnClickListener()
        viewDataBinding.dateTitle.setOnClickListener(selectTypeOnChangeListener())

    }


    private fun confirmOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            if (!dday.isEmpty()) {
                ddaySetListener.invoke(dday, viewDataBinding.calendarView.selectedDate)
            }
            this.dismiss()
        }
    }

    private fun cancelOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            this.dismiss()
        }
    }

    private fun selectTypeOnChangeListener() : View.OnClickListener {
        return View.OnClickListener {
            if(calendarVisible) {
                viewDataBinding.calendarView.visibility = View.GONE
                viewDataBinding.datePicker.visibility = View.VISIBLE
                calendarVisible = false
            }  else {
                viewDataBinding.calendarView.visibility = View.VISIBLE
                viewDataBinding.datePicker.visibility = View.GONE
                calendarVisible = true
            }
        }
    }
    private fun TextView.setSelectDate(year:String, month:String) {
        text = "${year}년 ${month}월"
    }

}