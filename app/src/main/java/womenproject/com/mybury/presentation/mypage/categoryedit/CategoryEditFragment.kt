package womenproject.com.mybury.presentation.mypage.categoryedit

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.databinding.FragmentCategoryEditBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.viewmodels.CategoryInfoViewModel
import womenproject.com.mybury.presentation.viewmodels.MyPageViewModel
import womenproject.com.mybury.ui.ItemDragListener

class CategoryEditFragment : BaseFragment<FragmentCategoryEditBinding, MyPageViewModel>(), ItemDragListener {


    private lateinit var editCategoryListAdapter: EditCategoryListAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    private val categoryInfoViewModel = CategoryInfoViewModel()

    override val layoutResourceId: Int
        get() = R.layout.fragment_category_edit

    override val viewModel: MyPageViewModel
        get() = MyPageViewModel()


    override fun initDataBinding() {
        viewDataBinding.backBtnOnClickListener = setOnBackBtnClickListener()
        setCategoryList()
    }

    private fun setCategoryList() {

        val categoryList = mutableListOf<String>()
        categoryList.add("없음")

        categoryInfoViewModel.getCategoryList(object : CategoryInfoViewModel.GetBucketListCallBackListener {
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

}