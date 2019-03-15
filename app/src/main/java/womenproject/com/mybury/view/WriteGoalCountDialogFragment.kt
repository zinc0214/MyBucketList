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
import womenproject.com.mybury.base.BaseDialogFragment
import womenproject.com.mybury.databinding.WriteGoalCountDialogBinding
import womenproject.com.mybury.ui.EditTextInputFilter

/**
 * Created by HanAYeon on 2019. 3. 13..
 */


@SuppressLint("ValidFragment")
class WriteGoalCountDialogFragment(private var currentCount : Int, private var goalCountSetListener: (String) -> Unit) : BaseDialogFragment<WriteGoalCountDialogBinding>() {

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.writeFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }


    override val layoutResourceId: Int
        get() = R.layout.write_goal_count_dialog

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

        viewDataBinding.goalCountSeekbar.setOnSeekBarChangeListener(setOnSeekbarChangedListener())
        viewDataBinding.goalCountEditText.setOnKeyListener(setOnEditTextEnterListener())
        viewDataBinding.bottomSheet.cancelButtonClickListener = cancelButtonClickListener()
        viewDataBinding.bottomSheet.confirmButtonClickListener = confirmButtonClickListener()

        viewDataBinding.goalCountEditText.filters = arrayOf(EditTextInputFilter("1", "100"))

        viewDataBinding.goalCountEditText.setText(currentCount.toString())
        viewDataBinding.goalCountSeekbar.progress = currentCount


    }



    private fun setOnSeekbarChangedListener() : SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewDataBinding.goalCountEditText.setText("$progress")
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
                        viewDataBinding.goalCountSeekbar.progress = viewDataBinding.goalCountEditText.text.toString().toInt()
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
            goalCountSetListener.invoke(viewDataBinding.goalCountEditText.text.toString())
            this.dismiss()
        }
    }

}