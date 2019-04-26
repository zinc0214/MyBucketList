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
import java.util.*


/**
 * Created by HanAYeon on 2019. 3. 8..
 */

@SuppressLint("ValidFragment")
class WriteCalendarDialogFragment(private var ddaySetListener: (String, Date) -> Unit, private var calendarDay: Date) : BaseDialogFragment<WriteCalendarDialogBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.write_calendar_dialog

    private var dday = ""

    private var selectDay = Date(0, 0, 0)
    private var senderDay = Date(0, 0, 0)
    private var today = CalendarDay.today()
    private var calendarVisible = true

    private lateinit var dayPicker: NumberPicker
    private lateinit var monthPicker: NumberPicker
    private lateinit var yearPicker: NumberPicker

    override fun initStartView() {


        viewDataBinding.calendarView.addDecorator(CurrentDateDecorator())
        viewDataBinding.calendarView.setDateTextAppearance(R.style.CalendarDate)
        viewDataBinding.calendarView.setWeekDayTextAppearance(R.style.CalendarWeek)
        viewDataBinding.calendarView.topbarVisible = false

        viewDataBinding.dateTitle.setSelectDate(calendarDay.year, calendarDay.month + 1)
        viewDataBinding.dateTitle.paintFlags = Paint.UNDERLINE_TEXT_FLAG


    }

    override fun initDataBinding() {

        viewDataBinding.calendarView.setCurrentDate(calendarDay)
        viewDataBinding.calendarView.setDateSelected(calendarDay, true)

        selectDay.year = viewDataBinding.calendarView.selectedDate.year
        selectDay.month = viewDataBinding.calendarView.selectedDate.month
        selectDay.date = viewDataBinding.calendarView.selectedDate.day


        Log.e("ayhan", "select : ${selectDay.year}/ ${selectDay.month} / ${selectDay.date}")
        Log.e("ayhan", "calendar : ${calendarDay} // ${calendarDay.year}}")
    }

    override fun initAfterBinding() {

        colorizeDatePicker(viewDataBinding.datePicker)
        dateTimePickerTextColour(viewDataBinding.datePicker, context!!.getColor(R.color.mainColor))


        viewDataBinding.calendarView.setOnDateChangedListener { widget, date, selected ->
            if (date.year < 2219) {
                setCurrentDateTitle(date.year, date.month, date.day)
            }
        }

        viewDataBinding.calendarView.setOnMonthChangedListener { widget, date ->
            if (date.year < 2219) {
                setCurrentDateTitle(date.year, date.month, date.day)
            }
        }

        yearPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            setCurrentDateTitle(picker.value, monthPicker.value, dayPicker.value)
        }

        monthPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            setCurrentDateTitle(yearPicker.value, picker.value, dayPicker.value)
        }

        dayPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            setCurrentDateTitle(yearPicker.value, monthPicker.value, picker.value)
        }

        viewDataBinding.bottomSheet.confirmButtonClickListener = confirmOnClickListener()
        viewDataBinding.bottomSheet.cancelButtonClickListener = cancelOnClickListener()
        viewDataBinding.dateTitle.setOnClickListener(selectTypeOnChangeListener())

    }


    private fun setCurrentDateTitle(dateYear: Int, dateMonth: Int, dateDay: Int) {
        var month = ""
        var day = ""

        month = if (dateMonth + 1 < 10) {
            "0${dateMonth + 1}"
        } else {
            (dateMonth + 1).toString()
        }

        day = if (dateDay < 10) {
            "0$dateDay"
        } else {
            dateDay.toString()
        }

        selectDay.year = dateYear
        selectDay.month = dateMonth
        selectDay.date = dateDay

        viewDataBinding.dateTitle.setSelectDate(dateYear, dateMonth + 1)
        dday = "$dateYear/$month/$day"

        Log.e("ayhan:cal_setCurrentDateTitle", "${selectDay.year}/${selectDay.month}/${selectDay.date}")
        Log.e("ayhan:dd", dday)


    }

    private fun confirmOnClickListener(): View.OnClickListener {
        return View.OnClickListener {

            if (!dday.isEmpty()) {
                senderDay.year = selectDay.year - 1900
                senderDay.month = selectDay.month
                senderDay.date = selectDay.date

                ddaySetListener.invoke(dday, senderDay)
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

            Log.e("ayhan", "textClick,SelectDay : ${selectDay.year} / ${selectDay.month} / ${selectDay.date}}")

            if (calendarVisible) {
                viewDataBinding.calendarView.visibility = View.GONE
                viewDataBinding.datePicker.visibility = View.VISIBLE

                yearPicker.value = selectDay.year
                monthPicker.value = selectDay.month
                dayPicker.value = selectDay.date

                Log.e("ayhan:datePickerVisible", "${selectDay.year}/${selectDay.month}/${selectDay.date}")

                calendarVisible = false
            } else {
                viewDataBinding.calendarView.visibility = View.VISIBLE
                viewDataBinding.datePicker.visibility = View.GONE

                viewDataBinding.calendarView.setCurrentDate(selectDay)
                viewDataBinding.calendarView.setDateSelected(selectDay, true)

                Log.e("ayhan:calendarVisible", "${selectDay.year}/${selectDay.month}/${selectDay.date}")

                calendarVisible = true
            }
        }
    }


    private fun TextView.setSelectDate(year: Int, month: Int) {
        val realYear = if (year < 1000) {
            year + 1900
        } else {
            year
        }
        text = "${realYear}년 ${month}월"
    }


    fun colorizeDatePicker(datePicker: DatePicker) {
        val system = Resources.getSystem()
        val dayId = system.getIdentifier("day", "id", "android")
        val monthId = system.getIdentifier("month", "id", "android")
        val yearId = system.getIdentifier("year", "id", "android")

        dayPicker = datePicker.findViewById(dayId) as NumberPicker
        monthPicker = datePicker.findViewById(monthId) as NumberPicker
        yearPicker = datePicker.findViewById(yearId) as NumberPicker

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
            } else if (t0 is ViewGroup) {
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