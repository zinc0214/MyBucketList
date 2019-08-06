package womenproject.com.mybury.view

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.base.BaseFragment
import womenproject.com.mybury.databinding.FragmentCategoryEditBinding
import womenproject.com.mybury.R
import womenproject.com.mybury.adapter.EditCategoryListAdapter
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.ui.CategoryItemTouchHelperCallback
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.viewmodels.BucketInfoViewModel
import womenproject.com.mybury.viewmodels.BucketWriteViewModel
import womenproject.com.mybury.viewmodels.MyPageViewModel

class CategoryEditFragment : BaseFragment<FragmentCategoryEditBinding, MyPageViewModel>(), ItemDragListener {


    private lateinit var editCategoryListAdapter: EditCategoryListAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    private val bucketInfoViewModel = BucketInfoViewModel()

    override val layoutResourceId: Int
        get() = R.layout.fragment_category_edit

    override val viewModel: MyPageViewModel
        get() = MyPageViewModel()


    override fun initStartView() {

    }

    override fun initDataBinding() {
        viewDataBinding.backBtnOnClickListener = setOnBackBtnClickListener()
    }

    override fun initAfterBinding() {

        val categoryList = mutableListOf<String>()
        categoryList.add("없음")

        bucketInfoViewModel.getCategoryList(object : BucketInfoViewModel.GetBucketListCallBackListener {
            override fun start() {

            }

            override fun success(bucketCategory: BucketCategory) {
                for(i in 0 until bucketCategory.categoryList.size) {
                    categoryList.add(bucketCategory.categoryList[i].name)
                }
                editCategoryListAdapter = EditCategoryListAdapter(categoryList, this@CategoryEditFragment)

                viewDataBinding.categoryListRecyclerView.apply {
                    adapter = editCategoryListAdapter
                    layoutManager = LinearLayoutManager(context)
                }


                itemTouchHelper = ItemTouchHelper(CategoryItemTouchHelperCallback(editCategoryListAdapter))
                itemTouchHelper.attachToRecyclerView(viewDataBinding.categoryListRecyclerView)
            }

            override fun fail() {

            }

        })

    }


    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    private fun setOnBackBtnClickListener() : View.OnClickListener {
        return View.OnClickListener {
            activity!!.onBackPressed()
        }
    }


}