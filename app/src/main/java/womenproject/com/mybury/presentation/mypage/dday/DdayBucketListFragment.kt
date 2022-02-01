package womenproject.com.mybury.presentation.mypage.dday

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.DdayBucketList
import womenproject.com.mybury.data.Preference.Companion.getDdayFilterForShow
import womenproject.com.mybury.databinding.FragmentDdayListBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.dialog.NetworkFailDialog
import womenproject.com.mybury.presentation.viewmodels.DdayBucketTotalListViewModel
import womenproject.com.mybury.ui.snackbar.MainSnackBarWidget

/**
 * Created by HanAYeon on 2019. 1. 16..
 */

@AndroidEntryPoint
class DdayBucketListFragment : BaseFragment<FragmentDdayListBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_dday_list

    private val viewModel by viewModels<DdayBucketTotalListViewModel>()

    override fun initDataBinding() {
        binding.ddayEachBucketList.layoutManager = LinearLayoutManager(context)
        binding.ddayEachBucketList.hasFixedSize()
        binding.backBtnOnClickListener = backBtnOnClickListener()
        binding.filterClickListener = filterOnClickListener()
        getDdayList()
    }

    private fun getDdayList() {
        getDdayFilterForShow(requireContext())?.let {
            viewModel.getDdayEachBucketList(object : BaseViewModel.MoreCallBackAnyList {
                override fun restart() {
                    getDdayList()
                }

                override fun start() {
                    startLoading()
                }

                override fun success(bucketList: List<Any>) {
                    stopLoading()
                    binding.ddayEachBucketList.adapter = DdayBucketTotalListAdapter(context, bucketList as List<DdayBucketList>, showSnackBar)
                }

                override fun fail() {
                    stopLoading()
                    NetworkFailDialog().show(requireActivity().supportFragmentManager)
                }
            }, it)
        }

    }


    private fun filterOnClickListener() = View.OnClickListener {
        val filterDialogFragment = DdayFilterDialogFragment(filterChangedListener)
        filterDialogFragment.show(requireActivity().supportFragmentManager, "tag")
    }

    private val filterChangedListener: () -> Unit = {
        getDdayList()
    }

    private fun setBucketCancel(bucketId: String) {
        viewModel.setBucketCancel(object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                setBucketCancel(bucketId)
            }

            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(requireActivity().supportFragmentManager)
            }

            override fun start() {
                startLoading()
            }

            override fun success() {
                getDdayList()
            }

        }, bucketId)

    }

    private fun bucketCancelListener(info: BucketItem) = View.OnClickListener {
        setBucketCancel(info.id)
    }

    private val showSnackBar: (BucketItem) -> Unit = { info: BucketItem ->
        showCancelSnackBar(requireView(), info)
    }

    private fun showCancelSnackBar(view: View, info: BucketItem) {
        val countText = if (info.goalCount > 1) "\" ${info.userCount}회 완료" else " \" 완료"
        MainSnackBarWidget.make(view, info.title, countText, bucketCancelListener(info))?.show()
    }

}