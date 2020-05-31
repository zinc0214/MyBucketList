package womenproject.com.mybury.presentation.mypage.dday

import android.app.ActionBar
import android.view.View
import android.widget.Toast
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference.Companion.getDdayFilterForShow
import womenproject.com.mybury.data.Preference.Companion.setDdayFilerForShow
import womenproject.com.mybury.data.ShowFilter
import womenproject.com.mybury.databinding.DdayFilterDialogBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment

class DdayFilterDialogFragment(private var stateChangeListener: () -> Unit) : BaseDialogFragment<DdayFilterDialogBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.dday_filter_dialog

    private var started = false
    private var complete = false

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialogFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    override fun initDataBinding() {
        viewDataBinding.apply {
            filterBoxClickListener = setOnCheckBoxChangedListener()
            filterSetClickListener = setOnFilterChangedListener()
        }
        initShowFilter()

    }

    private fun initShowFilter() {
        val filter = getDdayFilterForShow(context!!)

        when (filter) {
            "all" -> {
                viewDataBinding.startedCheckBox.isChecked = true
                viewDataBinding.completeCheckBox.isChecked = true
                started = true
                complete = true
            }
            "started" -> {
                viewDataBinding.startedCheckBox.isChecked = true
                viewDataBinding.completeCheckBox.isChecked = false
                started = true
                complete = false
            }
            "completed" -> {
                viewDataBinding.startedCheckBox.isChecked = false
                viewDataBinding.completeCheckBox.isChecked = true
                started = false
                complete = true
            }
        }
    }

    private fun setOnCheckBoxChangedListener() = View.OnClickListener {
        viewDataBinding.apply {
            when (it) {
                startedCheckBox -> started = startedCheckBox.isChecked
                completeCheckBox -> complete = completeCheckBox.isChecked
            }
        }
    }

    private fun setShowFilter() {
        if (started && complete) {
            setDdayFilerForShow(context!!, ShowFilter.all)
        } else if (started) {
            setDdayFilerForShow(context!!, ShowFilter.started)
        } else if (complete) {
            setDdayFilerForShow(context!!, ShowFilter.completed)
        }
    }

    private fun setOnFilterChangedListener(): View.OnClickListener {
        return View.OnClickListener {
            dialogDismiss()
        }
    }

    private fun dialogDismiss() {
        if (!started && !complete) {
            Toast.makeText(context, "표시할 버킷리스트가 최소 하나는 있어야합니다.", Toast.LENGTH_SHORT).show()
        } else {
            setShowFilter()
            stateChangeListener()
            this.dismiss()
        }

    }
}

