package womenproject.com.mybury.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import womenproject.com.mybury.R
import womenproject.com.mybury.adapter.SliderAdapter
import womenproject.com.mybury.base.BaseFragment
import womenproject.com.mybury.databinding.FragmentBucketWriteTestBinding
import womenproject.com.mybury.util.ScreenUtils
import womenproject.com.mybury.util.SliderLayoutManager
import womenproject.com.mybury.viewmodels.BucketWriteViewModelTest


/**
 * Created by HanAYeon on 2018. 12. 3..
 */

class BucketWriteFragmentTest : BaseFragment<FragmentBucketWriteTestBinding, BucketWriteViewModelTest>() {

    private var number = 0
    private var string = "_"
    private val data = (1..100).toList().map { it.toString() } as ArrayList<String>
    private lateinit var imm : InputMethodManager


    override val layoutResourceId: Int
        get() = R.layout.fragment_bucket_write_test

    override val viewModel: BucketWriteViewModelTest
        get() = BucketWriteViewModelTest()

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        imm = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        viewDataBinding.checkAdultListener = createOnAdultCheckBtnListener()
        viewDataBinding.numberCheckListener = numberCheckListener()
        setHorizontalPicker()
        setPickerText()
        setPickerText2()
        sliderIsScroll()
        initPosition()
    }



    @SuppressLint("ClickableViewAccessibility")
    private fun initPosition() {
        viewDataBinding.rvHorizontalPicker.scrollToPosition(1)
        viewDataBinding.userCount.setText("1")
    }

    private fun createOnAdultCheckBtnListener(): View.OnClickListener {
        return View.OnClickListener {

            if (viewDataBinding.bucketTitle.text.toString() == "") {
                Toast.makeText(context, "Please Write Text First.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.checkGo(viewDataBinding.bucketTitle.text.toString())
            }
        }
    }

    private fun numberCheckListener(): View.OnClickListener {
        return View.OnClickListener {
            viewDataBinding.userCount.setBackgroundColor(context!!.getColor(R.color.testColor))
            viewDataBinding.userCount.isCursorVisible = true
        }
    }

    private fun setHorizontalPicker() {
        // Setting the padding such that the items will appear in the middle of the screen
        val padding: Int = ScreenUtils.getScreenWidth(context) / 2 - ScreenUtils.dpToPx(context, 30)
        viewDataBinding.rvHorizontalPicker.setPadding(padding, 0, padding, 0)

        // Setting layout manager
        viewDataBinding.rvHorizontalPicker.layoutManager = SliderLayoutManager(context).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    viewDataBinding.tvSelectedItem.setText(data[layoutPosition])
                    viewDataBinding.userCount.setText(data[layoutPosition])
                    viewDataBinding.tvSelectedItem.setSelection(viewDataBinding.tvSelectedItem.text.length)
                    viewDataBinding.userCount.setSelection(viewDataBinding.userCount.text.length)
                    viewDataBinding.userCount.isCursorVisible = false
                    if (data[layoutPosition] != string && string != "_") {
                        viewDataBinding.rvHorizontalPicker.smoothScrollToPosition(number)
                        string = "_"
                    }

                }
            }
        }
        // Setting Adapter
        viewDataBinding.rvHorizontalPicker.adapter = SliderAdapter().apply {
            setData(data)
            callback = object : SliderAdapter.Callback {
                override fun onItemClicked(view: View) {
                    // Log.e("ayhan", "${binding.rvHorizontalPicker.getChildLayoutPosition(view)}")
                    viewDataBinding.rvHorizontalPicker.smoothScrollToPosition(viewDataBinding.rvHorizontalPicker.getChildLayoutPosition(view))
                }
            }
        }
    }

    private fun setPickerText() {
        viewDataBinding.tvSelectedItem.imeOptions = EditorInfo.IME_ACTION_DONE

        viewDataBinding.tvSelectedItem.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                string = viewDataBinding.tvSelectedItem.text.toString()
                number = string.toInt()
                Log.e("ayhan", "String : " + string + "Number : " + number)
                viewDataBinding.rvHorizontalPicker.smoothScrollToPosition(number)

                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
    }


    private fun setPickerText2() {
        viewDataBinding.userCount.imeOptions = EditorInfo.IME_ACTION_DONE

        viewDataBinding.userCount.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                string = viewDataBinding.userCount.text.toString()
                number = string.toInt()
                Log.e("ayhan", "String : " + string + "Number : " + number)
                viewDataBinding.rvHorizontalPicker.smoothScrollToPosition(number)
                imm.hideSoftInputFromWindow(viewDataBinding.userCount.windowToken, 0)
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
    }

    private fun sliderIsScroll() {
        viewDataBinding.rvHorizontalPicker.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.e("ayhan", "isScrollNow")
            viewDataBinding.userCount.setText("")
            viewDataBinding.userCount.setBackgroundColor(context!!.resources.getColor(R.color.textOriColor))
        }
    }
}