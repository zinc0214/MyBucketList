package womenproject.com.mybury.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.FragmentDdayListBinding
import womenproject.com.mybury.viewmodels.DdayBucketListViewModel

/**
 * Created by HanAYeon on 2019. 1. 16..
 */

class DdayBucketListFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentDdayListBinding>(
                inflater, R.layout.fragment_dday_list, container, false).apply {
            viewModel = DdayBucketListViewModel()

        }

        return binding.root
    }
}