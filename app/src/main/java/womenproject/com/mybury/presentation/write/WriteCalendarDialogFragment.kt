package womenproject.com.mybury.presentation.write

import android.annotation.SuppressLint
import android.app.ActionBar
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


    override fun onResume() {
        super.onResume()
        val dialogWidth = resources.getDimensionPixelSize(R.dimen.writeFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    override fun initDataBinding() {

        initCalendarDeco()

        binding.calendarView.setCurrentDate(calendarDay)
        binding.calendarView.setDateSelected(calendarDay, true)

        selectDay.year = binding.calendarView.selectedDate.year
        selectDay.month = binding.calendarView.selectedDate.month
        selectDay.date = binding.calendarView.selectedDate.day

        setCurrentDateTitle(calendarDay.year+1900, calendarDay.month, calendarDay.date)
        setCalendarViewListener()

    }

    private fun initCalendarDeco() {
        binding.calendarView.addDecorator(CurrentDateDecorator())
        binding.calendarView.setDateTextAppearance(R.style.CalendarDate)
        binding.calendarView.setWeekDayTextAppearance(R.style.CalendarWeek)
        binding.calendarView.topbarVisible = false

        binding.dateTitle.setSelectDate(calendarDay.year, calendarDay.month + 1)
        binding.dateTitle.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    private fun setCalendarViewListener() {

        colorizeDatePicker(binding.datePicker)
        dateTimePickerTextColour(binding.datePicker, requireContext().getColor(R.color._5a95ff))

        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            if (date.year < 2219) {
                setConfirmAble(date)
                setCurrentDateTitle(date.year, date.month, date.day)
            }
        }

        binding.calendarView.setOnMonthChangedListener { widget, date ->
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

        binding.bottomSheet.confirmButtonClickListener = confirmOnClickListener()
        binding.bottomSheet.cancelButtonClickListener = cancelOnClickListener()
        binding.dateTitle.setOnClickListener(selectTypeOnChangeListener())

        binding.leftArrowClickListener = leftArrowOnClickListener()
        binding.rightArrowClickListener = rightArrowOnClickListener()


    }


    private fun setConfirmAble(date: CalendarDay) {
        binding.bottomSheet.confirmText.isEnabled = date.date >= today.date
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

        binding.dateTitle.setSelectDate(dateYear, dateMonth + 1)
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
                binding.calendarView.visibility = View.GONE
                binding.datePicker.visibility = View.VISIBLE
                binding.leftArrowImg.visibility = View.GONE
                binding.rightArrowImg.visibility = View.GONE

                yearPicker.value = selectDay.year
                monthPicker.value = selectDay.month
                dayPicker.value = selectDay.date

                calendarVisible = false
            } else {
                binding.calendarView.visibility = View.VISIBLE
                binding.datePicker.visibility = View.GONE
                binding.leftArrowImg.visibility = View.VISIBLE
                binding.rightArrowImg.visibility = View.VISIBLE

                binding.calendarView.setCurrentDate(selectDay)
                binding.calendarView.setDateSelected(selectDay, true)

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