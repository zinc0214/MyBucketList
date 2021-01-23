package womenproject.com.mybury.presentation.write

import android.annotation.SuppressLint
import android.app.ActionBar
import android.graphics.Rect
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.SeekBar
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.DialogWriteFragmentGoalCountBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import womenproject.com.mybury.ui.EditTextInputFilter

/**
 * Created by HanAYeon on 2019. 3. 13..
 */


@SuppressLint("ValidFragment")
class WriteGoalCountDialogFragment(private var currentCount: Int, private var goalCountSetListener: (String) -> Unit) : BaseDialogFragment<DialogWriteFragmentGoalCountBinding>() {

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.writeFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }


    override val layoutResourceId: Int
        get() = R.layout.dialog_write_fragment_goal_count


    override fun initDataBinding() {
        viewDataBinding.root.viewTreeObserver.addOnGlobalLayoutListener(setOnSoftKeyboardChangedListener())

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
        return View.OnKeyListener { v, keyCode, event ->
            if (event != null) {
                if((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    viewDataBinding.goalCountSeekbar.progress = if(viewDataBinding.goalCountEditText.text.isNotEmpty()) {
                        viewDataBinding.goalCountEditText.text.toString().toInt()
                    } else {
                        1
                    }
                }
            }
            false
        }
    }

    private fun cancelButtonClickListener() : View.OnClickListener {
        return View.OnClickListener {
            this.dismiss()
        }
    }

    private fun confirmButtonClickListener() : View.OnClickListener {
        return View.OnClickListener {
            goalCountSetListener.invoke(if(viewDataBinding.goalCountEditText.text.isNotEmpty()) {
                viewDataBinding.goalCountEditText.text.toString()
            } else {
                "1"
            })
            this.dismiss()
        }
    }

    private fun setOnSoftKeyboardChangedListener(): ViewTreeObserver.OnGlobalLayoutListener {
        return ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            viewDataBinding.root.getWindowVisibleDisplayFrame(r)

            val heightDiff = viewDataBinding.root.rootView.height - (r.bottom - r.top)
            try {
                if (heightDiff < 300) {
                    viewDataBinding.goalCountSeekbar.progress = if(viewDataBinding.goalCountEditText.text.isNotEmpty()) {
                        viewDataBinding.goalCountEditText.text.toString().toInt()
                    } else {
                        1
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}