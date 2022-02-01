package womenproject.com.mybury.presentation.main

import android.app.ActionBar
import android.view.View
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference.Companion.getFilterForShow
import womenproject.com.mybury.data.Preference.Companion.getFilterListUp
import womenproject.com.mybury.data.Preference.Companion.getShowDdayFilter
import womenproject.com.mybury.data.Preference.Companion.setFilerForShow
import womenproject.com.mybury.data.Preference.Companion.setFilterListUp
import womenproject.com.mybury.data.Preference.Companion.setShowDdayFilter
import womenproject.com.mybury.data.ShowFilter
import womenproject.com.mybury.data.SortFilter
import womenproject.com.mybury.databinding.DialogMainFilterBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment

/**
 * Created by HanAYeon on 2019. 1. 15..
 */

@AndroidEntryPoint
open class FilterDialogFragment(private var stateChangeListener: () -> Unit) :
    BaseDialogFragment<DialogMainFilterBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.dialog_main_filter

    private var started = false
    private var complete = false
    private var sortType: SortFilter = SortFilter.createdDt
    private var dday = false

    override fun initDataBinding() {
        viewDataBinding.fragment = this
        viewDataBinding.filterSetClickListener = createOnClickFilterSetListener()
        viewDataBinding.filterBoxListener = setOnCheckBoxChangedListener()
        initShowFilter()
        initListUpFilter()
        initSortListener()
        initShowDdayState()
    }

    private fun initShowFilter() {
        val filter = getFilterForShow(requireContext())

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
        when (getFilterListUp(requireContext())) {
            "updatedDt" -> {
                viewDataBinding.radioBtnUpdate.isChecked = true
            }
            "createdDt" -> {
                viewDataBinding.radioBtnCreate.isChecked = true
            }
            "custom" -> {
                viewDataBinding.radioBtnCustom.isChecked = true
            }
        }
    }

    private fun initSortListener() {
        viewDataBinding.sortRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.radio_btn_update -> {
                    sortType = SortFilter.updatedDt
                }
                R.id.radio_btn_create -> {
                    sortType = SortFilter.createdDt
                }
                R.id.radio_btn_custom -> {
                    sortType = SortFilter.custom
                }
            }
        }
    }

    private fun initShowDdayState() {
        val filter = getShowDdayFilter(requireContext())
        viewDataBinding.showDdayState.isChecked = filter
        dday = filter
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
            setShowDdayState()
            stateChangeListener()
            this.dismiss()
        }

    }

    private fun setOnCheckBoxChangedListener() = View.OnClickListener {
        viewDataBinding.apply {
            when (it) {
                startedCheckBox -> started = startedCheckBox.isChecked
                completeCheckBox -> complete = completeCheckBox.isChecked
                showDdayState -> dday = showDdayState.isChecked
            }
        }
    }

    private fun setShowFilter() {
        if (started && complete) {
            setFilerForShow(requireContext(), ShowFilter.all)
        } else if (started) {
            setFilerForShow(requireContext(), ShowFilter.started)
        } else if (complete) {
            setFilerForShow(requireContext(), ShowFilter.completed)
        }
    }

    private fun setListUpFilter() {
        setFilterListUp(requireContext(), sortType)
    }

    private fun setShowDdayState() {
        setShowDdayFilter(requireContext(), dday)
    }

}