package womenproject.com.mybury.view

import android.icu.util.Calendar
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import womenproject.com.mybury.R
import womenproject.com.mybury.base.BaseFragment
import womenproject.com.mybury.databinding.FragmentBucketWriteBinding
import womenproject.com.mybury.ui.WriteImgLayout
import womenproject.com.mybury.viewmodels.BucketWriteViewModel
import android.R.attr.button
import androidx.cardview.widget.CardView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class BucketWriteFragment : BaseFragment<FragmentBucketWriteBinding, BucketWriteViewModel>() {

    private var addImgList = HashMap<Int, CardView
            >()
    private var ddayCount = 1
    private var currentCalendarDay = CalendarDay.today()

    override val layoutResourceId: Int
        get() = R.layout.fragment_bucket_write

    override val viewModel: BucketWriteViewModel
        get() = BucketWriteViewModel()

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

        viewDataBinding.cancelBtnClickListener = writeCancelOnClickListener()
        viewDataBinding.memoImgAddListener = memoImgAddOnClickListener()
        viewDataBinding.memoRemoveListener = memoRemoveListener()
        viewDataBinding.ddayAddListener = ddayAddListener()
        viewDataBinding.goalCountSettingListener = goalCountSetListener()

        viewDataBinding.titleText.addTextChangedListener(titleTextChangedListener(viewDataBinding.titleText))
        viewDataBinding.memoText.addTextChangedListener(memoTextChangedListener(viewDataBinding.memoText))
        viewDataBinding.memoRemoveImg.setOnTouchListener(memoRemoveOnTouchListener())

        viewDataBinding.memoText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                viewDataBinding.memoRemoveImg.visibility = View.INVISIBLE
            } else {
                viewDataBinding.memoRemoveImg.visibility = View.VISIBLE
            }
        }

        viewDataBinding.openSwitchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewDataBinding.openText.text = context!!.resources.getString(R.string.bucket_open)
                viewDataBinding.openImg.background = context!!.getDrawable(R.drawable.open_enable)
            } else {
                viewDataBinding.openText.text = context!!.resources.getString(R.string.bucket_close)
                viewDataBinding.openImg.background = context!!.getDrawable(R.drawable.open_disable)
            }
        }

    }

    private fun writeCancelOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            activity!!.onBackPressed()
        }
    }

    private fun titleTextChangedListener(editText: EditText): TextWatcher {

        return object : TextWatcher {
            var previousString = ""

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                previousString = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.lineCount > 2) {
                    editText.setText(previousString)
                    editText.setSelection(editText.length())
                }
                viewDataBinding.writeRegist.isEnabled = editText.length() >= 1
            }
        }
    }

    private fun memoTextChangedListener(editText: EditText): TextWatcher {

        return object : TextWatcher {
            var previousString = ""

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                previousString = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.lineCount > 2) {
                    editText.setText(previousString)
                    editText.setSelection(editText.length())
                }
            }
        }
    }

    private fun memoRemoveOnTouchListener(): View.OnTouchListener {

        return View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewDataBinding.memoLayout.background = context!!.getDrawable(R.drawable.write_memo_press_background)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    viewDataBinding.memoLayout.background = context!!.getDrawable(R.drawable.write_memo_background)
                }
            }
            false
        }
    }

    private fun memoImgAddOnClickListener(): View.OnClickListener {

        val memoAddListener: () -> Unit = {
            viewDataBinding.memoLayout.visibility = View.VISIBLE
        }

        val checkAddImgAbleListener: () -> Boolean = {
            Log.e("ayhan", ":count ${viewDataBinding.imgLayout.childCount}")
            if (viewDataBinding.imgLayout.childCount > 2) {
                Toast.makeText(context, "더 이상 이미지를 추가하실 수 없습니다.", Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        }

        val imgAddListener: (Uri) -> Unit = {
            onAddImgField(it)
        }

        return View.OnClickListener {
            WriteMemoImgAddDialogFragment(memoAddListener, checkAddImgAbleListener, imgAddListener).show(activity!!.supportFragmentManager, "tag")
        }
    }


    private fun onAddImgField(uri: Uri) {

        val removeImgListener: (View) -> Unit = { it ->
            onDeleteImgField(it)
        }

        val writeImgLayout = WriteImgLayout(this.context!!, removeImgListener).setUI(uri)
        addImgList.put(viewDataBinding.imgLayout.childCount, writeImgLayout as CardView)
        viewDataBinding.imgLayout.addView(writeImgLayout)
    }

    private fun onDeleteImgField(layout: View) {
        var deleteImgValue = 0

        for (i in 0..addImgList.size) {
            if (layout.equals(addImgList[i])) {
                deleteImgValue = i
            }
        }
        viewDataBinding.imgLayout.removeView(viewDataBinding.imgLayout.getChildAt(deleteImgValue))
        addImgList.remove(deleteImgValue)
    }

    private fun memoRemoveListener(): View.OnClickListener {

        return View.OnClickListener {
            viewDataBinding.memoLayout.visibility = View.GONE
        }
    }

    private fun ddayAddListener(): View.OnClickListener {

        val ddayAddListener: (String, CalendarDay) -> Unit = { dday, calendarDay ->
            viewDataBinding.ddayText.text = dday
            currentCalendarDay = calendarDay
            setTextColor(viewDataBinding.ddayText)
            viewDataBinding.ddayImg.background = context!!.getDrawable(R.drawable.calendar_enable)
        }

        return View.OnClickListener {
            val calendarDialogFragment = CalendarDialogFragment(ddayAddListener, currentCalendarDay)
            calendarDialogFragment.show(activity!!.supportFragmentManager, "tag")
        }
    }

    private fun goalCountSetListener(): View.OnClickListener {
        val goalCountSetListener: (String) -> Unit = { count ->
            viewDataBinding.goalCountText.text = count
            ddayCount = count.toInt()
            if (count.toInt() != 1) {
                setTextColor(viewDataBinding.goalCountText)
                viewDataBinding.countImg.background = context!!.getDrawable(R.drawable.target_count_enable)

            }
        }

        return View.OnClickListener {
            WriteGoalCountDialogFragment(ddayCount, goalCountSetListener).show(activity!!.supportFragmentManager, "tag")
        }
    }

    private fun setTextColor(textView: TextView) {
        textView.setTextColor(context!!.resources.getColor(R.color.mainColor))
    }

}