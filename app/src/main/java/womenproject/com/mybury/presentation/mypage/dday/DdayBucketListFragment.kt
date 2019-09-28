package womenproject.com.mybury.presentation.mypage.dday

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.databinding.FragmentDdayListBinding
import womenproject.com.mybury.presentation.viewmodels.DdayBucketTotalListViewModel

/**
 * Created by HanAYeon on 2019. 1. 16..
 */

class DdayBucketListFragment : BaseFragment<FragmentDdayListBinding, DdayBucketTotalListViewModel>() {

    private val BUCKETLIST_API = "http://10.1.101.161/host/"

    override val layoutResourceId: Int
        get() = R.layout.fragment_dday_list

    override val viewModel: DdayBucketTotalListViewModel
        get() = DdayBucketTotalListViewModel()

    override fun initDataBinding() {

        val layoutManager = LinearLayoutManager(context)

        viewDataBinding.ddayEachBucketList.layoutManager = layoutManager
        viewDataBinding.ddayEachBucketList.hasFixedSize()
       // viewDataBinding.ddayEachBucketList.adapter = DdayBucketTotalListAdapter(context, DdayBucketTotalListViewModel().getDdayEachBucketItem())


        viewModel.getDdayEachBucketList(BUCKETLIST_API, object : DdayBucketTotalListViewModel.OnDdayBucketListGetEvent{
            override fun start() {
                viewDataBinding.progressBar.visibility = View.VISIBLE
            }

            override fun finish(bucketList: BucketList?) {
                if(bucketList != null)  {
                    viewDataBinding.progressBar.visibility = View.GONE
                    viewDataBinding.ddayEachBucketList.adapter = DdayBucketTotalListAdapter(context, bucketList)
                }
            }
        })
    }
}