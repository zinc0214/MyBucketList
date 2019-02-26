package womenproject.com.mybury.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.adapter.DdayBucketTotalListAdapter
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.databinding.FragmentDdayListBinding
import womenproject.com.mybury.viewmodels.DdayBucketTotalListViewModel

/**
 * Created by HanAYeon on 2019. 1. 16..
 */

class DdayBucketListFragment : BaseFragment() {

    private lateinit var ddayBucketTotalListViewModel: DdayBucketTotalListViewModel
    private lateinit var binding: FragmentDdayListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        ddayBucketTotalListViewModel = DdayBucketTotalListViewModel(context)

        binding = DataBindingUtil.inflate<FragmentDdayListBinding>(
                inflater, R.layout.fragment_dday_list, container, false)


        binding.apply {
            viewModel = ddayBucketTotalListViewModel
            initBucketListUI()

        }


        return binding.root
    }


    private fun initBucketListUI(){
        val layoutManager = LinearLayoutManager(context)

        binding.ddayEachBucketList.layoutManager = layoutManager
        binding.ddayEachBucketList.hasFixedSize()
       // binding.ddayEachBucketList.adapter = DdayBucketTotalListAdapter(context, ddayBucketTotalListViewModel.getDdayEachBucketItem())


        ddayBucketTotalListViewModel.getDdayEachBucketList(object : DdayBucketTotalListViewModel.OnDdayBucketListGetEvent{
            override fun start() {
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun finish(bucketList: BucketList?) {
                if(bucketList != null)  {
                    binding.progressBar.visibility = View.GONE
                    binding.ddayEachBucketList.adapter = DdayBucketTotalListAdapter(context, bucketList)
                }
            }
        })
    }
}