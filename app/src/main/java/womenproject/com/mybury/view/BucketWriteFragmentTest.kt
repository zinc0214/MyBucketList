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
import womenproject.com.mybury.databinding.FragmentBucketWriteTestBinding
import womenproject.com.mybury.util.ScreenUtils
import womenproject.com.mybury.util.SliderLayoutManager
import womenproject.com.mybury.viewmodels.BucketWriteViewModelTest


/**
 * Created by HanAYeon on 2018. 12. 3..
 */

class BucketWriteFragmentTest : BaseFragment() {

    private lateinit var bucketWriteViewModelTest: BucketWriteViewModelTest
    private lateinit var binding: FragmentBucketWriteTestBinding
    private var number = 0
    private var string = "_"
    private val data = (1..100).toList().map { it.toString() } as ArrayList<String>
    private lateinit var imm : InputMethodManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        bucketWriteViewModelTest = BucketWriteViewModelTest(context)
        binding = DataBindingUtil.inflate<FragmentBucketWriteTestBinding>(
                inflater, R.layout.fragment_bucket_write_test, container, false).apply {
            viewModel = bucketWriteViewModelTest
        }

        imm = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.apply {
            checkAdultListener = createOnAdultCheckBtnListener()
            numberCheckListener = numberCheckListener()
            setHorizontalPicker()
            setPickerText()
            setPickerText2()
            sliderIsScroll()
            initPosition()

        }

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initPosition() {
        binding.rvHorizontalPicker.scrollToPosition(1)
        binding.userCount.setText("1")
    }

    private fun createOnAdultCheckBtnListener(): View.OnClickListener {
        return View.OnClickListener {

            if (binding.bucketTitle.text.toString() == "") {
                Toast.makeText(context, "Please Write Text First.", Toast.LENGTH_SHORT).show()
            } else {
                bucketWriteViewModelTest.checkGo(binding.bucketTitle.text.toString())
            }
        }
    }

    private fun numberCheckListener(): View.OnClickListener {
        return View.OnClickListener {
            binding.userCount.setBackgroundColor(context!!.getColor(R.color.testColor))
            binding.userCount.isCursorVisible = true
        }
    }

    private fun setHorizontalPicker() {
        // Setting the padding such that the items will appear in the middle of the screen
        val padding: Int = ScreenUtils.getScreenWidth(context) / 2 - ScreenUtils.dpToPx(context, 30)
        binding.rvHorizontalPicker.setPadding(padding, 0, padding, 0)

        // Setting layout manager
        binding.rvHorizontalPicker.layoutManager = SliderLayoutManager(context).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    binding.tvSelectedItem.setText(data[layoutPosition])
                    binding.userCount.setText(data[layoutPosition])
                    binding.tvSelectedItem.setSelection(binding.tvSelectedItem.text.length)
                    binding.userCount.setSelection(binding.userCount.text.length)
                    binding.userCount.isCursorVisible = false
                    if (data[layoutPosition] != string && string != "_") {
                        binding.rvHorizontalPicker.smoothScrollToPosition(number)
                        string = "_"
                    }

                }
            }
        }
        // Setting Adapter
        binding.rvHorizontalPicker.adapter = SliderAdapter().apply {
            setData(data)
            callback = object : SliderAdapter.Callback {
                override fun onItemClicked(view: View) {
                    // Log.e("ayhan", "${binding.rvHorizontalPicker.getChildLayoutPosition(view)}")
                    binding.rvHorizontalPicker.smoothScrollToPosition(binding.rvHorizontalPicker.getChildLayoutPosition(view))
                }
            }
        }
    }

    private fun setPickerText() {
        binding.tvSelectedItem.imeOptions = EditorInfo.IME_ACTION_DONE

        binding.tvSelectedItem.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                string = binding.tvSelectedItem.text.toString()
                number = string.toInt()
                Log.e("ayhan", "String : " + string + "Number : " + number)
                binding.rvHorizontalPicker.smoothScrollToPosition(number)

                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
    }


    private fun setPickerText2() {
        binding.userCount.imeOptions = EditorInfo.IME_ACTION_DONE

        binding.userCount.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                string = binding.userCount.text.toString()
                number = string.toInt()
                Log.e("ayhan", "String : " + string + "Number : " + number)
                binding.rvHorizontalPicker.smoothScrollToPosition(number)
                imm.hideSoftInputFromWindow(binding.userCount.windowToken, 0)
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
    }

    private fun sliderIsScroll() {
        binding.rvHorizontalPicker.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.e("ayhan", "isScrollNow")
            binding.userCount.setText("")
            binding.userCount.setBackgroundColor(context!!.resources.getColor(R.color.textOriColor))
        }
    }
}