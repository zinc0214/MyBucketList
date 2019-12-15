package womenproject.com.mybury.presentation.mypage

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.MyPageCategory
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
import womenproject.com.mybury.databinding.FragmentBucketListByCategoryBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel

class BucketListByCategoryFragment  : BaseFragment<FragmentBucketListByCategoryBinding, BucketInfoViewModel>() {

    private lateinit var selectCategory : MyPageCategory

    override val layoutResourceId: Int
    get() = R.layout.fragment_bucket_list_by_category

    override val viewModel: BucketInfoViewModel
    get() = BucketInfoViewModel()

    override fun initDataBinding() {

        arguments?.let {
            val args = BucketListByCategoryFragmentArgs.fromBundle(it)
            val category = args.category
            this.selectCategory = category!!
        }

        viewDataBinding.headerLayout.title = selectCategory.name

        initBucketListUI()
    }


    private fun initBucketListUI () {
        val layoutManager = LinearLayoutManager(context)

        viewDataBinding.bucketList.layoutManager = layoutManager
        viewDataBinding.bucketList.hasFixedSize()

        Log.e("ayhan", "categoryId : $selectCategory")

        viewModel.getBucketListByCategory(object : BucketInfoViewModel.OnBucketListGetEvent {
            override fun fail() {
                stopLoading()
            }

            override fun start() {
                startLoading()
            }

            override fun finish(bucketList: List<BucketItem>) {
                viewDataBinding.bucketList.adapter = CategoryBucketListAdapter(context, bucketList)
                stopLoading()
            }
        }, getAccessToken(context!!), selectCategory.id)

        //   viewDataBinding.bucketList.adapter = MainBucketListAdapter(context, viewModel.getMainBucketList())
        //  viewDataBinding.progressBar.visibility = View.GONE

    }
}