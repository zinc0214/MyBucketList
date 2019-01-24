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
import womenproject.com.mybury.databinding.FragmentBucketWriteBinding
import womenproject.com.mybury.util.ScreenUtils
import womenproject.com.mybury.util.SliderLayoutManager
import womenproject.com.mybury.viewmodels.BucketWriteViewModel


/**
 * Created by HanAYeon on 2018. 12. 3..
 */

class BucketWriteFragment : BaseFragment() {

    private lateinit var bucketWriteViewModel: BucketWriteViewModel
    private lateinit var binding: FragmentBucketWriteBinding
    private var number = 0
    private var string = "_"
    private val data = (1..100).toList().map { it.toString() } as ArrayList<String>
    private lateinit var imm : InputMethodManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        bucketWriteViewModel = BucketWriteViewModel(context)
        binding = DataBindingUtil.inflate<FragmentBucketWriteBinding>(
                inflater, R.layout.fragment_bucket_write, container, false).apply {
            viewModel = bucketWriteViewModel
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
        binding.currentNum.setText("1")
    }

    private fun createOnAdultCheckBtnListener(): View.OnClickListener {
        return View.OnClickListener {

            if (binding.bucketTitle.text.toString() == "") {
                Toast.makeText(context, "Please Write Text First.", Toast.LENGTH_SHORT).show()
            } else {
                bucketWriteViewModel.checkGo(binding.bucketTitle.text.toString())
            }
        }
    }

    private fun numberCheckListener(): View.OnClickListener {
        return View.OnClickListener {
            binding.currentNum.setBackgroundColor(context!!.getColor(R.color.testColor))
            binding.currentNum.isCursorVisible = true
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
                    binding.currentNum.setText(data[layoutPosition])
                    binding.tvSelectedItem.setSelection(binding.tvSelectedItem.text.length)
                    binding.currentNum.setSelection(binding.currentNum.text.length)
                    binding.currentNum.isCursorVisible = false
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
        binding.currentNum.imeOptions = EditorInfo.IME_ACTION_DONE

        binding.currentNum.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                string = binding.currentNum.text.toString()
                number = string.toInt()
                Log.e("ayhan", "String : " + string + "Number : " + number)
                binding.rvHorizontalPicker.smoothScrollToPosition(number)
                imm.hideSoftInputFromWindow(binding.currentNum.windowToken, 0)
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
    }

    private fun sliderIsScroll() {
        binding.rvHorizontalPicker.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.e("ayhan", "isScrollNow")
            binding.currentNum.setText("")
            binding.currentNum.setBackgroundColor(context!!.resources.getColor(R.color.textOriColor))
        }
    }
}