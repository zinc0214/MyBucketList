package womenproject.com.mybury.presentation.mypage

import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.MyPageCategory
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
import womenproject.com.mybury.databinding.FragmentBucketListByCategoryBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel
import womenproject.com.mybury.presentation.viewmodels.MainFragmentViewModel

class BucketListByCategoryFragment  : BaseFragment<FragmentBucketListByCategoryBinding, MainFragmentViewModel>() {

    private val bucketInfoViewModel = BucketInfoViewModel()
    private lateinit var selectCategory : MyPageCategory

    override val layoutResourceId: Int
    get() = R.layout.fragment_bucket_list_by_category

    override val viewModel: MainFragmentViewModel
    get() = MainFragmentViewModel()

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

        bucketInfoViewModel.getBucketListByCategory(object : BucketInfoViewModel.OnBucketListGetEvent {
            override fun fail() {
                stopLoading()
              /*  Toast.makeText(context, "아이쿠, 데이터가 없나봐요! 그래서 더미 데이터를 준비했습니다!", Toast.LENGTH_SHORT).show()
                val list = viewModel.getDummyMainBucketList()
                list.last().isLast = true
                val listR = list.filter { bucketItem -> bucketItem.category.name == selectCategory.name }

                Log.e("ayhan", "list : ${listR.size}")

                if(listR.isEmpty()) {
                    activity!!.onBackPressed()
                    Toast.makeText(context, "아무 버킷이 없다.", Toast.LENGTH_SHORT).show()
                } else {
                    viewDataBinding.bucketList.adapter = CategoryBucketListeAdapter(context,listR)
                }
*/
            }

            override fun start() {
                startLoading()
            }

            override fun finish(bucketList: List<BucketItem>) {
                viewDataBinding.bucketList.adapter = CategoryBucketListeAdapter(context, bucketList)
                stopLoading()
            }
        }, getAccessToken(context!!), selectCategory.id)

        //   viewDataBinding.bucketList.adapter = MainBucketListAdapter(context, viewModel.getMainBucketList())
        //  viewDataBinding.progressBar.visibility = View.GONE

    }
}