package womenproject.com.mybury.view

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.NumberPicker
import android.widget.TextView
import com.prolificinteractive.materialcalendarview.CalendarDay
import womenproject.com.mybury.R
import womenproject.com.mybury.base.BaseDialogFragment
import womenproject.com.mybury.databinding.WriteCalendarDialogBinding
import womenproject.com.mybury.ui.CurrentDateDecorator
import android.widget.EditText
import android.view.ViewGroup


/**
 * Created by HanAYeon on 2019. 3. 8..
 */

@SuppressLint("ValidFragment")
class WriteCalendarDialogFragment(private var ddaySetListener: (String, CalendarDay) -> Unit, private var calendarDay: CalendarDay) : BaseDialogFragment<WriteCalendarDialogBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.write_calendar_dialog

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

        colorizeDatePicker(viewDataBinding.datePicker)
        dateTimePickerTextColour(viewDataBinding.datePicker, context!!.getColor(R.color.mainColor))


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
            viewDataBinding.dateTitle.setSelectDate(date.year.toString(), (date.month + 1).toString())
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

    private fun selectTypeOnChangeListener(): View.OnClickListener {
        return View.OnClickListener {
            if (calendarVisible) {
                viewDataBinding.calendarView.visibility = View.GONE
                viewDataBinding.datePicker.visibility = View.VISIBLE
                calendarVisible = false
            } else {
                viewDataBinding.calendarView.visibility = View.VISIBLE
                viewDataBinding.datePicker.visibility = View.GONE
                calendarVisible = true
            }
        }
    }

    private fun TextView.setSelectDate(year: String, month: String) {
        text = "${year}년 ${month}월"
    }


    fun colorizeDatePicker(datePicker: DatePicker) {
        val system = Resources.getSystem()
        val dayId = system.getIdentifier("day", "id", "android")
        val monthId = system.getIdentifier("month", "id", "android")
        val yearId = system.getIdentifier("year", "id", "android")

        val dayPicker = datePicker.findViewById(dayId) as NumberPicker
        val monthPicker = datePicker.findViewById(monthId) as NumberPicker
        val yearPicker = datePicker.findViewById(yearId) as NumberPicker

        setDividerColor(dayPicker)
        setDividerColor(monthPicker)
        setDividerColor(yearPicker)
    }

    private fun setDividerColor(picker: NumberPicker) {
        val count = picker.getChildCount()
        for (i in 0 until count) {
            try {
                val dividerField = picker::class.java.getDeclaredField("mSelectionDivider")
                dividerField.setAccessible(true)
                val colorDrawable = ColorDrawable(picker.getResources().getColor(R.color.writeSeekbarBackground2))
                val height = resources.getDimensionPixelSize(R.dimen.titleMargin)
                dividerField.set(picker, colorDrawable)
                picker.invalidate()
            } catch (e: Exception) {
                Log.w("setDividerColor", e)
            }

        }
    }


    private fun dateTimePickerTextColour(`$picker`: ViewGroup, `$color`: Int) {

        var i = 0
        val j = `$picker`.childCount
        while (i < j) {
            val t0 = `$picker`.getChildAt(i) as View

            if (t0 is NumberPicker) {
                numberPickerTextColor(t0, `$color`)
            }
            else if (t0 is ViewGroup) {
                dateTimePickerTextColour(t0, `$color`)
            }
            i++
        }
    }

    private fun numberPickerTextColor(`$v`: NumberPicker, `$c`: Int) {
        var i = 0
        val j = `$v`.childCount
        while (i < j) {
            val t0 = `$v`.getChildAt(i)
            if (t0 is EditText) {
                try {
                    val t1 = `$v`.javaClass.getDeclaredField("mSelectorWheelPaint")
                    t1.isAccessible = true
                    (t1.get(`$v`) as Paint).setColor(`$c`)
                    t0.setTextColor(`$c`)
                    `$v`.invalidate()
                } catch (e: Exception) {
                }

            }
            i++
        }
    }


}