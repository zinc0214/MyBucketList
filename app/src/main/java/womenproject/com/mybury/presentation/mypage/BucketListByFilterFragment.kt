package womenproject.com.mybury.presentation.mypage

import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference.Companion.getFilterListUp
import womenproject.com.mybury.data.ShowFilter
import womenproject.com.mybury.databinding.FragmentBucketListByCategoryBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel

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

        viewModel.getMainBucketList(object : BaseViewModel.MoreCallBackAnyList {
            override fun restart() {
                initBucketListUI()
            }

            override fun fail() {
                stopLoading()
            }

            override fun start() {
                startLoading()
            }

            override fun success(value: List<Any>) {
                viewDataBinding.bucketList.adapter = CategoryBucketListAdapter(value as List<BucketItem>)
                stopLoading()
            }
        }, filterType.toString(), getFilterListUp(context!!))

    }
}