package womenproject.com.mybury.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.adapter.DdayBucketTotalListAdapter
import womenproject.com.mybury.databinding.FragmentDdayListBinding
import womenproject.com.mybury.viewmodels.DdayBucketTotalListViewModel

/**
 * Created by HanAYeon on 2019. 1. 16..
 */

class DdayBucketListFragment : BaseFragment() {

    private lateinit var ddayBucketTotalListViewModel: DdayBucketTotalListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        ddayBucketTotalListViewModel = DdayBucketTotalListViewModel()

        val binding = DataBindingUtil.inflate<FragmentDdayListBinding>(
                inflater, R.layout.fragment_dday_list, container, false).apply {
            viewModel = DdayBucketTotalListViewModel()
            ddayEachBucketList.layoutManager = LinearLayoutManager(context)
            ddayEachBucketList.hasFixedSize()
            ddayEachBucketList.adapter =  DdayBucketTotalListAdapter(context, ddayBucketTotalListViewModel.getDdayEachBucketList())


        }

        return binding.root
    }

}