package womenproject.com.mybury.presentation.mypage.dday

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.DdayBucketList
import womenproject.com.mybury.data.Preference.Companion.getDdayFilterForShow
import womenproject.com.mybury.databinding.FragmentDdayListBinding
import womenproject.com.mybury.presentation.NetworkFailDialog
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.viewmodels.DdayBucketTotalListViewModel
import womenproject.com.mybury.ui.snackbar.MainSnackBarWidget

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
                viewDataBinding.ddayEachBucketList.adapter = DdayBucketTotalListAdapter(context, bucketList as List<DdayBucketList>, showSnackBar)
            }

            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(activity!!.supportFragmentManager)
            }
        }, getDdayFilterForShow(context!!))
    }


    private fun filterOnClickListener() = View.OnClickListener {
        val filterDialogFragment = DdayFilterDialogFragment(filterChangedListener)
        filterDialogFragment.show(activity!!.supportFragmentManager, "tag")
    }

    private val filterChangedListener: () -> Unit = {
        getDdayList()
    }

    private fun setBucketCancel(bucketId : String) {
        viewModel.setBucketCancel(object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                setBucketCancel(bucketId)
            }

            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(activity!!.supportFragmentManager)
            }

            override fun start() {
                startLoading()
            }

            override fun success() {
                getDdayList()
            }

        }, bucketId)

    }

    private fun bucketCancelListener(info : BucketItem) = View.OnClickListener {
        Toast.makeText(activity, "info : ${info.title}", Toast.LENGTH_SHORT).show()
        setBucketCancel(info.id)
    }

    private val showSnackBar: (BucketItem) -> Unit = { info : BucketItem ->
        showCancelSnackBar(view!!, info)
    }

    private fun showCancelSnackBar(view: View, info : BucketItem) {
        val countText = if(info.userCount > 1) "${info.userCount}회 완료" else "완료"
        MainSnackBarWidget.make(view, info.title, countText, bucketCancelListener(info))?.show()
    }

}