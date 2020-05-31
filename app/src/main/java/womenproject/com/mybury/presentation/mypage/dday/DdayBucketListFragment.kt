package womenproject.com.mybury.presentation.mypage.dday

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.data.DdayBucketList
import womenproject.com.mybury.databinding.FragmentDdayListBinding
import womenproject.com.mybury.generated.callback.OnClickListener
import womenproject.com.mybury.presentation.NetworkFailDialog
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.main.FilterDialogFragment
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
        viewDataBinding.ddayEachBucketList.layoutManager = LinearLayoutManager(context)
        viewDataBinding.ddayEachBucketList.hasFixedSize()
        viewDataBinding.backBtnOnClickListener = backBtnOnClickListener()
        viewDataBinding.filterClickListener = filterOnClickListener()
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


    private fun filterOnClickListener() = View.OnClickListener {
        val filterDialogFragment = DdayFilterDialogFragment(filterChangedListener)
        filterDialogFragment.show(activity!!.supportFragmentManager, "tag")
    }

    private val filterChangedListener: () -> Unit = {
        getDdayList()
    }

}