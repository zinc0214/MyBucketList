package womenproject.com.mybury.presentation.main

import android.app.ActionBar
import android.util.Log
import android.view.View
import android.widget.Toast
import womenproject.com.mybury.R
import womenproject.com.mybury.data.ListUpFilter
import womenproject.com.mybury.data.Preference.Companion.getFilterForShow
import womenproject.com.mybury.data.Preference.Companion.getFilterListUp
import womenproject.com.mybury.data.Preference.Companion.setFilerForShow
import womenproject.com.mybury.data.Preference.Companion.setFilterListUp
import womenproject.com.mybury.data.ShowFilter
import womenproject.com.mybury.databinding.MainFilterDialogBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment

/**
 * Created by HanAYeon on 2019. 1. 15..
 */


open class FilterDialogFragment(private var stateChangeListener: () -> Unit) : BaseDialogFragment<MainFilterDialogBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.main_filter_dialog

    private var started = false
    private var complete = false
    private var update = false
    private var written = false


    override fun initDataBinding() {
        viewDataBinding.fragment = this
        viewDataBinding.filterSetClickListener = createOnClickFilterSetListener()
        viewDataBinding.filterBoxListener = setOnCheckBoxChangedListener()
        initShowFilter()
        initListUpFilter()

    }

    private fun initShowFilter() {
        val filter = getFilterForShow(context!!)

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


    private fun initListUpFilter() {
        val filter = getFilterListUp(context!!)
        when (filter) {
            "updatedDt" -> {
                viewDataBinding.radioBtnUpdate.isChecked = true
                viewDataBinding.radioBtnWritten.isChecked = false
                update = true
                written = false
            }
            else -> {
                viewDataBinding.radioBtnUpdate.isChecked = false
                viewDataBinding.radioBtnWritten.isChecked = true
                update = false
                written = true
            }
        }

    }

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialogFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    private fun createOnClickFilterSetListener(): View.OnClickListener {
        return View.OnClickListener {
            dialogDismiss()
        }
    }

    private fun dialogDismiss() {
        if (!started && !complete) {
            Toast.makeText(context, "표시할 버킷리스트가 최소 하나는 있어야합니다.", Toast.LENGTH_SHORT).show()
        } else {
            setShowFilter()
            setListUpFilter()
            stateChangeListener()
            this.dismiss()
        }

    }

    private fun setOnCheckBoxChangedListener() = View.OnClickListener {
        Log.e("ayhan", "${it}")

        when (it) {
            viewDataBinding.startedCheckBox -> started = viewDataBinding.startedCheckBox.isChecked
            viewDataBinding.completeCheckBox -> complete = viewDataBinding.completeCheckBox.isChecked
            viewDataBinding.radioBtnUpdate,
            viewDataBinding.radioBtnWritten -> {
                update = viewDataBinding.radioBtnUpdate.isChecked
                written = viewDataBinding.radioBtnWritten.isChecked
            }
        }
    }


    private fun setShowFilter() {
        if (started && complete) {
            setFilerForShow(context!!, ShowFilter.all)
        } else if (started) {
            setFilerForShow(context!!, ShowFilter.started)
        } else if (complete) {
            setFilerForShow(context!!, ShowFilter.completed)
        }
    }

    private fun setListUpFilter() {
        Log.e("ayhan", "checkFilter : ${update}, $written")
        if (update) {
            setFilterListUp(context!!, ListUpFilter.updatedDt)
        } else {
            setFilterListUp(context!!, ListUpFilter.createdDt)
        }
    }

}