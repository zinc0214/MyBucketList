package womenproject.com.mybury.presentation.mypage.dday

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.data.DdayBucketList
import womenproject.com.mybury.databinding.FragmentDdayListBinding
import womenproject.com.mybury.presentation.NetworkFailDialog
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.viewmodels.DdayBucketTotalListViewModel

/**
 * Created by HanAYeon on 2019. 1. 16..
 */

class DdayBucketListFragment : BaseFragment<FragmentDdayListBinding, DdayBucketTotalListViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_dday_list

    override val viewModel: DdayBucketTotalListViewModel
        get() = DdayBucketTotalListViewModel()

    override fun initDataBinding() {

        val layoutManager = LinearLayoutManager(context)

        viewDataBinding.ddayEachBucketList.layoutManager = layoutManager
        viewDataBinding.ddayEachBucketList.hasFixedSize()
        viewDataBinding.ddayToolbar.title = "D-day"
        viewDataBinding.ddayToolbar.backBtnOnClickListener = backBtnOnClickListener()
        getDdayList()

    }

    private fun getDdayList() {
        viewModel.getDdayEachBucketList(object : BaseViewModel.MoreCallBackAnyList {
            override fun restart() {
                getDdayList()
            }

            override fun start() {
                startLoading()
            }

            override fun success(bucketList: List<Any>) {
                stopLoading()
                viewDataBinding.ddayEachBucketList.adapter = DdayBucketTotalListAdapter(context, bucketList as List<DdayBucketList>)
            }

            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(activity!!.supportFragmentManager)
            }

        })

    }

}