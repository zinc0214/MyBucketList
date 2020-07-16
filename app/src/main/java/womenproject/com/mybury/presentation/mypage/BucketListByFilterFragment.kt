package womenproject.com.mybury.presentation.mypage

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.Preference.Companion.getFilterListUp
import womenproject.com.mybury.data.ShowFilter
import womenproject.com.mybury.databinding.FragmentBucketListByCategoryBinding
import womenproject.com.mybury.presentation.NetworkFailDialog
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel
import womenproject.com.mybury.ui.snackbar.MainSnackBarWidget

class BucketListByFilterFragment  : BaseFragment<FragmentBucketListByCategoryBinding, BucketInfoViewModel>() {

    private lateinit var filterType : String

    override val layoutResourceId: Int
    get() = R.layout.fragment_bucket_list_by_category

    override val viewModel: BucketInfoViewModel
    get() = BucketInfoViewModel()

    override fun initDataBinding() {

        arguments?.let {
            val args = BucketListByFilterFragmentArgs.fromBundle(it)
            val filter = args.filter
            this.filterType = filter!!
        }

        viewDataBinding.headerLayout.title =
            when(filterType) {
                ShowFilter.started.toString() -> "진행중"
                else -> "완료"
            }
        viewDataBinding.headerLayout.backBtnOnClickListener = backBtnOnClickListener()

        initBucketListUI()
    }

    private fun initBucketListUI () {
        val layoutManager = LinearLayoutManager(context)

        viewDataBinding.bucketList.layoutManager = layoutManager
        viewDataBinding.bucketList.hasFixedSize()

        getFilterListUp(context!!)?.let {
            viewModel.getMainBucketList(object : BaseViewModel.MoreCallBackAny {
                override fun restart() {
                    initBucketListUI()
                }

                override fun fail() {
                    stopLoading()
                }

                override fun start() {
                    startLoading()
                }

                override fun success(value: Any) {
                    val response = value as BucketList
                    viewDataBinding.bucketList.adapter = CategoryBucketListAdapter(response.bucketlists, showSnackBar)
                    stopLoading()
                }
            }, filterType, it)
        }


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
                initBucketListUI()
            }

        }, bucketId)

    }

    private fun bucketCancelListener(info : BucketItem) = View.OnClickListener {
        setBucketCancel(info.id)
    }

    private val showSnackBar: (BucketItem) -> Unit = { info : BucketItem ->
        showCancelSnackBar(requireView(), info)
    }

    private fun showCancelSnackBar(view: View, info : BucketItem) {
        val countText = if (info.goalCount > 1) "${info.userCount}회 완료" else "완료"
        MainSnackBarWidget.make(view, info.title, countText, bucketCancelListener(info))?.show()
    }

}