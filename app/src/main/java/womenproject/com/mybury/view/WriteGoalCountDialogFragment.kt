package womenproject.com.mybury.view

import android.annotation.SuppressLint
import android.app.ActionBar
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.WriteGoalCountDialogBinding
import womenproject.com.mybury.ui.EditTextInputFilter

/**
 * Created by HanAYeon on 2019. 3. 13..
 */


@SuppressLint("ValidFragment")
class WriteGoalCountDialogFragment(private var goalCountSetListener: (String) -> Unit) : DialogFragment() {

    private lateinit var dday: String
    private lateinit var binding : WriteGoalCountDialogBinding

    companion object {

        fun instance(goalCountSetListener: (String) -> Unit): WriteGoalCountDialogFragment {
            val fragment = WriteGoalCountDialogFragment(goalCountSetListener)

            return fragment
        }
    }

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.writeFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<WriteGoalCountDialogBinding>(
                inflater, R.layout.write_goal_count_dialog, container, false).apply {

            goalCountSeekbar.setOnSeekBarChangeListener(setOnSeekbarChangedListener())
            goalCountEditText.setOnKeyListener(setOnEditTextEnterListener())
            bottomSheet.cancelButtonClickListener = cancelButtonClickListener()
            bottomSheet.confirmButtonClickListener = confirmButtonClickListener()

            goalCountEditText.isCursorVisible = false
            goalCountEditText.filters = arrayOf(EditTextInputFilter("1", "100"))

        }


        return binding.root
    }


    private fun setOnSeekbarChangedListener() : SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.goalCountEditText.setText("$progress")
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        }
    }

    private fun setOnEditTextEnterListener() : View.OnKeyListener {
        return object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event != null) {
                    if((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        binding.goalCountSeekbar.progress = binding.goalCountEditText.text.toString().toInt()
                    }
                }
                return false
            }


        }
    }

    private fun cancelButtonClickListener() : View.OnClickListener {
        return View.OnClickListener {
            this.dismiss()
        }
    }

    private fun confirmButtonClickListener() : View.OnClickListener {
        return View.OnClickListener {
            goalCountSetListener.invoke(binding.goalCountEditText.text.toString())
            this.dismiss()
        }
    }

}