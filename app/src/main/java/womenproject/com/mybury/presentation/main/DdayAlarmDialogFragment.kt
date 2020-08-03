package womenproject.com.mybury.presentation.main

import android.app.ActionBar
import android.text.Html
import android.util.Log
import android.view.View
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference.Companion.setCloseAlarm3Days
import womenproject.com.mybury.databinding.MainDdayAlarmDialogBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import java.util.*

class DdayAlarmDialogFragment(private var goToDday: () -> Unit) : BaseDialogFragment<MainDdayAlarmDialogBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.main_dday_alarm_dialog

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialogFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    override fun initDataBinding() {
        viewDataBinding.apply {
            setDoToDdayClickListener {
                goToDday()
                dismiss()
            }
            close3DaysClickListener = closeAlarm3Days
        }
    }

    private val closeAlarm3Days = View.OnClickListener {
        val time = Date().time
        setCloseAlarm3Days(context!!, time)
        dismiss()
    }
}
