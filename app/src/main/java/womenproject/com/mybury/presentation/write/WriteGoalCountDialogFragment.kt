package womenproject.com.mybury.presentation.write

import android.app.ActionBar
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.DialogWriteFragmentGoalCountBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import womenproject.com.mybury.ui.EditTextInputFilter


/**
 * Created by HanAYeon on 2019. 3. 13..
 */
class WriteGoalCountDialogFragment(
    private var currentCount: Int,
    private var goalCountSetListener: (Int) -> Unit
) : BaseDialogFragment<DialogWriteFragmentGoalCountBinding>() {

    lateinit var imm: InputMethodManager

    override fun onResume() {
        super.onResume()
        val dialogWidth = resources.getDimensionPixelSize(R.dimen.writeFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    override val layoutResourceId: Int
        get() = R.layout.dialog_write_fragment_goal_count


    override fun initDataBinding() {
        imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.goalCountEditText.apply {
            addTextChangedListener(addTextChangedListener())
            filters = arrayOf(EditTextInputFilter("1", "1000000"))
            if (currentCount > 1) {
                setText(currentCount.toString())
            }
            setOnKeyListener(setOnEditTextEnterListener())
            setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    binding.goalCountLayout.setBackgroundResource(R.drawable.shape_ffffff_r4_strk_06_5a95ff)
                } else {
                    binding.goalCountLayout.setBackgroundResource(R.drawable.shape_ffffff_r4_strk_06_d8d8d8)
                }
            }
        }

        binding.bottomSheet.cancelButtonClickListener = cancelButtonClickListener()
        binding.bottomSheet.confirmButtonClickListener = confirmButtonClickListener()

    }

    private fun addTextChangedListener(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                if (text == "0" || text == "1") {
                    binding.goalCountEditText.setTextColor(
                        resources.getColor(
                            R.color._b4b4b4,
                            null
                        )
                    )
                } else {
                    binding.goalCountEditText.setTextColor(
                        resources.getColor(
                            R.color._5a95ff,
                            null
                        )
                    )
                }
            }
        }
    }

    private fun setOnEditTextEnterListener(): View.OnKeyListener {
        return View.OnKeyListener { _, keyCode, event ->
            if (event != null) {
                if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    binding.goalCountEditText.clearFocus()
                    imm.hideSoftInputFromWindow(binding.goalCountEditText.windowToken, 0)
                }
            }
            false
        }
    }

    private fun cancelButtonClickListener() = View.OnClickListener { this.dismiss() }

    private fun confirmButtonClickListener(): View.OnClickListener {
        return View.OnClickListener {
            goalCountSetListener.invoke(
                if (binding.goalCountEditText.text.isNotEmpty() && binding.goalCountEditText.text.toString() != "0"
                ) {
                    binding.goalCountEditText.text.toString().toInt()
                } else {
                    1
                }
            )
            this.dismiss()
        }
    }
}