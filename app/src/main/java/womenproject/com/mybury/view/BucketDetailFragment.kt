package womenproject.com.mybury.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.FragmentBucketDetailBinding
import womenproject.com.mybury.viewmodels.BucketDetailViewModel

/**
 * Created by HanAYeon on 2018. 11. 30..
 */

class BucketDetailFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val bucketId = BucketDetailFragmentArgs.fromBundle(arguments).bucketId
        val binding = DataBindingUtil.inflate<FragmentBucketDetailBinding>(
                inflater, R.layout.fragment_bucket_detail, container, false).apply {
            viewModel = BucketDetailViewModel(bucketId)

        }

        return binding.root
    }
}