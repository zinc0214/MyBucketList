package womenproject.com.mybury.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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

    lateinit var bucketWriteViewModel: BucketWriteViewModel
    lateinit var binding: FragmentBucketWriteBinding
    lateinit var sliderLayoutManager: SliderLayoutManager
    var number = 0
    private val data = (1..100).toList().map { it.toString() } as ArrayList<String>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        bucketWriteViewModel = BucketWriteViewModel(context)
        binding = DataBindingUtil.inflate<FragmentBucketWriteBinding>(
                inflater, R.layout.fragment_bucket_write, container, false).apply {
            viewModel = bucketWriteViewModel
        }

        binding.apply {
            checkAdultListener = createOnAdultCheckBtnListener()
            setHorizontalPicker()
            setPickerText()
        }


        return binding.root
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


    private fun setHorizontalPicker() {
        // Setting the padding such that the items will appear in the middle of the screen
        val padding: Int = ScreenUtils.getScreenWidth(context) / 2 - ScreenUtils.dpToPx(context, 30)
        binding.rvHorizontalPicker.setPadding(padding, 0, padding, 0)

        // Setting layout manager
        sliderLayoutManager = SliderLayoutManager(context).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    binding.tvSelectedItem.setText(data[layoutPosition])
                    binding.tvSelectedItem.setSelection(binding.tvSelectedItem.text.length)
                }
            }
        }

        binding.rvHorizontalPicker.layoutManager = sliderLayoutManager

        // Setting Adapter
        binding.rvHorizontalPicker.adapter = SliderAdapter().apply {
            setData(data)
            callback = object : SliderAdapter.Callback {
                override fun onItemClicked(view: View) {
                    binding.rvHorizontalPicker.smoothScrollToPosition(binding.rvHorizontalPicker.getChildLayoutPosition(view))
                }
            }
        }
    }

    private fun setPickerText() {
        binding.tvSelectedItem.imeOptions = EditorInfo.IME_ACTION_DONE

        binding.tvSelectedItem.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                val string : String = binding.tvSelectedItem.text.toString()
                number = string.toInt()
                Log.e("ayhan", "String : " + string + "Number : " + number)
                binding.rvHorizontalPicker.smoothScrollToPosition(number)

                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
    }
}