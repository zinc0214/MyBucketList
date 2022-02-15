package womenproject.com.mybury.presentation.main

import android.app.ActionBar
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference.Companion.setCloseAlarm3Days
import womenproject.com.mybury.databinding.DialogMainDdayAlarmBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import java.util.*

@AndroidEntryPoint
class DdayAlarmDialogFragment(private var goToDday: () -> Unit) : BaseDialogFragment<DialogMainDdayAlarmBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.dialog_main_dday_alarm

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
        setCloseAlarm3Days(requireContext(), time)
        dismiss()
    }
}
