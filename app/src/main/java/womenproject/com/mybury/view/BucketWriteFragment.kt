package womenproject.com.mybury.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.FragmentBucketWriteBinding
import womenproject.com.mybury.viewmodels.BucketWriteViewModel

/**
 * Created by HanAYeon on 2018. 12. 3..
 */

class BucketWriteFragment : BaseFragment() {

    lateinit var bucketWriteViewModel : BucketWriteViewModel
    lateinit var binding : FragmentBucketWriteBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        bucketWriteViewModel = BucketWriteViewModel(context)
        binding = DataBindingUtil.inflate<FragmentBucketWriteBinding>(
                inflater, R.layout.fragment_bucket_write, container, false).apply {
           viewModel = bucketWriteViewModel
        }

        binding.apply {
            checkAdultListener = createOnAdultCheckBtnListener()
        }

        return binding.root
    }


    private fun createOnAdultCheckBtnListener(): View.OnClickListener {
        return View.OnClickListener {

            if(binding.bucketTitle.text.toString() == "") {
                Toast.makeText(context, "Please Write Text First.", Toast.LENGTH_SHORT).show()
            } else {
                bucketWriteViewModel.checkGo(binding.bucketTitle.text.toString())
            }
        }
    }

}