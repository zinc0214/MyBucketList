package womenproject.com.mybury.presentation.write

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.dialog_write_fragment_calendar.*
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.DialogWriteFragmentCalendarBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import womenproject.com.mybury.ui.CurrentDateDecorator
import java.util.*


/**
 * Created by HanAYeon on 2019. 3. 8..
 */

@SuppressLint("ValidFragment")
class WriteCalendarDialogFragment(private var ddaySetListener: (String, Date) -> Unit, private var calendarDay: Date) : BaseDialogFragment<DialogWriteFragmentCalendarBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.dialog_write_fragment_calendar

    private var dday = ""

    private val today = CalendarDay.today()
    private var selectDay = Date(0, 0, 0)
    private var senderDay = Date(0, 0, 0)
    private var calendarVisible = true

    private lateinit var dayPicker: NumberPicker
    private lateinit var monthPicker: NumberPicker
    private lateinit var yearPicker: NumberPicker


    override fun initDataBinding() {

        initCalendarDeco()

        viewDataBinding.calendarView.setCurrentDate(calendarDay)
        viewDataBinding.calendarView.setDateSelected(calendarDay, true)

        selectDay.year = viewDataBinding.calendarView.selectedDate.year
        selectDay.month = viewDataBinding.calendarView.selectedDate.month
        selectDay.date = viewDataBinding.calendarView.selectedDate.day

        setCurrentDateTitle(calendarDay.year+1900, calendarDay.month, calendarDay.date)
        setCalendarViewListener()

    }

    private fun initCalendarDeco() {
        viewDataBinding.calendarView.addDecorator(CurrentDateDecorator())
        viewDataBinding.calendarView.setDateTextAppearance(R.style.CalendarDate)
        viewDataBinding.calendarView.setWeekDayTextAppearance(R.style.CalendarWeek)
        viewDataBinding.calendarView.topbarVisible = false

        viewDataBinding.dateTitle.setSelectDate(calendarDay.year, calendarDay.month + 1)
        viewDataBinding.dateTitle.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    private fun setCalendarViewListener() {

        colorizeDatePicker(viewDataBinding.datePicker)
        dateTimePickerTextColour(viewDataBinding.datePicker, requireContext().getColor(R.color._5a95ff))

        viewDataBinding.calendarView.setOnDateChangedListener { widget, date, selected ->
            if (date.year < 2219) {
                setConfirmAble(date)
                setCurrentDateTitle(date.year, date.month, date.day)
            }
        }

        viewDataBinding.calendarView.setOnMonthChangedListener { widget, date ->
            if (date.year < 2219) {
                setConfirmAble(date)
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

        viewDataBinding.leftArrowClickListener = leftArrowOnClickListener()
        viewDataBinding.rightArrowClickListener = rightArrowOnClickListener()


    }


    private fun setConfirmAble(date: CalendarDay) {
        viewDataBinding.bottomSheet.confirmText.isEnabled = date.date >= today.date
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
            ddaySetListener.invoke("", calendarDay)
            this.dismiss()
        }
    }

    private fun selectTypeOnChangeListener(): View.OnClickListener {
        return View.OnClickListener {

            if (calendarVisible) {
                viewDataBinding.calendarView.visibility = View.GONE
                viewDataBinding.datePicker.visibility = View.VISIBLE
                viewDataBinding.leftArrowImg.visibility = View.GONE
                viewDataBinding.rightArrowImg.visibility = View.GONE

                yearPicker.value = selectDay.year
                monthPicker.value = selectDay.month
                dayPicker.value = selectDay.date

                calendarVisible = false
            } else {
                viewDataBinding.calendarView.visibility = View.VISIBLE
                viewDataBinding.datePicker.visibility = View.GONE
                viewDataBinding.leftArrowImg.visibility = View.VISIBLE
                viewDataBinding.rightArrowImg.visibility = View.VISIBLE

                viewDataBinding.calendarView.setCurrentDate(selectDay)
                viewDataBinding.calendarView.setDateSelected(selectDay, true)

                calendarVisible = true
            }
        }
    }


    private fun leftArrowOnClickListener() : View.OnClickListener {
        return View.OnClickListener {
            calendarView.goToPrevious()
        }
    }

    private fun rightArrowOnClickListener() : View.OnClickListener {
        return View.OnClickListener {
            calendarView.goToNext()
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
                val colorDrawable = ColorDrawable(picker.getResources().getColor(R.color._a6c6ff))
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