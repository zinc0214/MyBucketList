package womenproject.com.mybury.presentation.mypage

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.databinding.FragmentBucketListByCategoryBinding
import womenproject.com.mybury.databinding.FragmentMainBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.main.FilterDialogFragment
import womenproject.com.mybury.presentation.main.bucketlist.MainBucketListAdapter
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel
import womenproject.com.mybury.presentation.viewmodels.MainFragmentViewModel
import womenproject.com.mybury.presentation.mypage.BucketListByCategoryFragmentArgs

class BucketListByCategoryFragment  : BaseFragment<FragmentBucketListByCategoryBinding, MainFragmentViewModel>() {

    private val bucketInfoViewModel = BucketInfoViewModel()
    private var categoryName : String = "없음"

    override val layoutResourceId: Int
    get() = R.layout.fragment_bucket_list_by_category

    override val viewModel: MainFragmentViewModel
    get() = MainFragmentViewModel()

    override fun initDataBinding() {

        arguments?.let {
            val args = BucketListByCategoryFragmentArgs.fromBundle(it)
            val category = args.category
            categoryName = category!!
        }

        viewDataBinding.headerLayout.title = categoryName

        initBucketListUI()
    }


    private fun initBucketListUI () {
        val layoutManager = LinearLayoutManager(context)

        viewDataBinding.bucketList.layoutManager = layoutManager
        viewDataBinding.bucketList.hasFixedSize()

        Log.e("ayhan", "categoryId : $categoryName")

        bucketInfoViewModel.getMainBucketListByCategory(object : BucketInfoViewModel.OnBucketListGetEvent {
            override fun fail() {
                viewDataBinding.progressBar.visibility = View.GONE
                Toast.makeText(context, "아이쿠, 데이터가 없나봐요! 그래서 더미 데이터를 준비했습니다!", Toast.LENGTH_SHORT).show()
                val list = viewModel.getDummyMainBucketList()
                list.last().isLast = true
                val listR = list.filter { bucketItem -> bucketItem.category.name == categoryName }

                Log.e("ayhan", "list : ${listR.size}")

                if(listR.isEmpty()) {
                    activity!!.onBackPressed()
                    Toast.makeText(context, "아무 버킷이 없다.", Toast.LENGTH_SHORT).show()
                } else {
                    viewDataBinding.bucketList.adapter = CategoryBucketListeAdapter(context,listR)
                }

            }

            override fun start() {
                viewDataBinding.progressBar.visibility = View.VISIBLE
            }

            override fun finish(bucketList: List<BucketItem>) {
                if(bucketList != null)  {
                    viewDataBinding.progressBar.visibility = View.GONE
                    viewDataBinding.bucketList.adapter = CategoryBucketListeAdapter(context, bucketList)
                }
            }
        }, categoryName)

        //   viewDataBinding.bucketList.adapter = MainBucketListAdapter(context, viewModel.getMainBucketList())
        //  viewDataBinding.progressBar.visibility = View.GONE

    }
}