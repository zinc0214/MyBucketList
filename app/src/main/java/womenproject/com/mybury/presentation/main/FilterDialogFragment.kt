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

    private var sortType: SortFilter = SortFilter.createdDt
    private var showType: ShowFilter? = ShowFilter.all
    private var dday = false

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialogFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    override fun initDataBinding() {
        viewDataBinding.fragment = this
        viewDataBinding.filterSetClickListener = createOnClickFilterSetListener()
        viewDataBinding.filterBoxListener = setOnCheckBoxChangedListener()

        initFilter()
        initSortListener()
    }

    private fun initFilter() {
        val showFilter = getShowFilterType(getFilterForShow(requireContext()) ?: "all")
        val sortFilter = getSortFilterType(getFilterListUp(requireContext()) ?: "updatedDt")

        viewDataBinding.showFilter = showFilter
        viewDataBinding.sortFilter = sortFilter
        viewDataBinding.isDdayShow = getShowDdayFilter(requireContext())

        sortType = sortFilter
        showType = showFilter
    }


    private fun createOnClickFilterSetListener() = View.OnClickListener {
        dialogDismiss()
    }

    private fun dialogDismiss() {
        if (showType == null) {
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
                startedCheckBox -> updateShowFilter()
                completeCheckBox -> updateShowFilter()
                showDdayState -> dday = showDdayState.isChecked
            }
        }
    }

    private fun updateShowFilter() {
        viewDataBinding.apply {
            showType = if(startedCheckBox.isChecked && completeCheckBox.isChecked) {
                ShowFilter.all
            } else if(startedCheckBox.isChecked) {
                ShowFilter.started
            } else if(completeCheckBox.isChecked){
                ShowFilter.completed
            } else {
                null
            }
        }
    }
    private fun initSortListener() {
        viewDataBinding.sortRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.radio_btn_update -> sortType = SortFilter.updatedDt
                R.id.radio_btn_create -> sortType = SortFilter.createdDt
                R.id.radio_btn_custom -> sortType = SortFilter.custom
            }
        }
    }

    private fun setShowFilter() {
        showType?.let {
            setFilerForShow(requireContext(), it)
        }
    }

    private fun setListUpFilter() {
        setFilterListUp(requireContext(), sortType)
    }

    private fun setShowDdayState() {
        setShowDdayFilter(requireContext(), dday)
    }


    private fun getShowFilterType(sortTypeString: String): ShowFilter {
        return when (sortTypeString) {
            "all" -> {
                ShowFilter.all
            }
            "completed" -> {
                ShowFilter.completed
            }
            else -> {
                ShowFilter.started
            }
        }
    }

    private fun getSortFilterType(sortTypeString: String): SortFilter {
        return when (sortTypeString) {
            "updatedDt" -> {
                SortFilter.updatedDt
            }
            "createdDt" -> {
                SortFilter.createdDt
            }
            else -> {
                SortFilter.custom
            }
        }
    }

}