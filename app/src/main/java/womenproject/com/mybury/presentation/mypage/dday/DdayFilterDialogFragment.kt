package womenproject.com.mybury.presentation.mypage.dday

import android.app.ActionBar
import android.view.View
import android.widget.Toast
import womenproject.com.mybury.R
import womenproject.com.mybury.data.DdayShowFilter
import womenproject.com.mybury.data.Preference.Companion.getDdayFilterForShow
import womenproject.com.mybury.data.Preference.Companion.setDdayFilerForShow
import womenproject.com.mybury.databinding.DialogDdayFilterBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment

class DdayFilterDialogFragment(private var stateChangeListener: () -> Unit) : BaseDialogFragment<DialogDdayFilterBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.dialog_dday_filter

    private var plus = false
    private var minus = false

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialogFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    override fun initDataBinding() {
        binding.apply {
            filterBoxClickListener = setOnCheckBoxChangedListener()
            filterSetClickListener = setOnFilterChangedListener()
        }
        initShowFilter()

    }

    private fun initShowFilter() {
        val filter = getDdayFilterForShow(requireContext())

        when (filter) {
            "all" -> {
                binding.plusCheckBox.isChecked = true
                binding.minusCheckBox.isChecked = true
                plus = true
                minus = true
            }
            "minus" -> {
                binding.plusCheckBox.isChecked = false
                binding.minusCheckBox.isChecked = true
                plus = false
                minus = true
            }
            "plus" -> {
                binding.plusCheckBox.isChecked = true
                binding.minusCheckBox.isChecked = false
                plus = true
                minus = false
            }
        }
    }

    private fun setOnCheckBoxChangedListener() = View.OnClickListener {
        binding.apply {
            when (it) {
                plusCheckBox -> plus = plusCheckBox.isChecked
                minusCheckBox -> minus = minusCheckBox.isChecked
            }
        }
    }

    private fun setShowFilter() {
        if (plus &&  minus) {
            setDdayFilerForShow(requireContext(), DdayShowFilter.all)
        } else if (minus) {
            setDdayFilerForShow(requireContext(), DdayShowFilter.minus)
        } else if (plus) {
            setDdayFilerForShow(requireContext(), DdayShowFilter.plus)
        }
    }

    private fun setOnFilterChangedListener(): View.OnClickListener {
        return View.OnClickListener {
            dialogDismiss()
        }
    }

    private fun dialogDismiss() {
        if (!plus && !minus) {
            Toast.makeText(context, "표시할 버킷리스트가 최소 하나는 있어야합니다.", Toast.LENGTH_SHORT).show()
        } else {
            setShowFilter()
            stateChangeListener()
            this.dismiss()
        }

    }
}

