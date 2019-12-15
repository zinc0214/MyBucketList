package womenproject.com.mybury.presentation.mypage.categoryedit

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
import womenproject.com.mybury.data.Preference.Companion.getUserId
import womenproject.com.mybury.databinding.FragmentCategoryEditBinding
import womenproject.com.mybury.presentation.NetworkFailDialog
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel
import womenproject.com.mybury.presentation.viewmodels.CategoryInfoViewModel
import womenproject.com.mybury.presentation.viewmodels.MyPageViewModel
import womenproject.com.mybury.ui.ItemCheckedListener
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.ui.ItemMovedListener

class CategoryEditFragment : BaseFragment<FragmentCategoryEditBinding, MyPageViewModel>(), ItemDragListener, ItemCheckedListener, ItemMovedListener {

    private lateinit var editCategoryListAdapter: EditCategoryListAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    private val bucketInfoViewModel = BucketInfoViewModel()
    private val categoryEditViewModel = CategoryInfoViewModel()
    private val removedList = hashSetOf<String>()
    private var changeCategoryList = arrayListOf<Category>()

    override val layoutResourceId: Int
        get() = R.layout.fragment_category_edit

    override val viewModel: MyPageViewModel
        get() = MyPageViewModel()


    override fun initDataBinding() {
        viewDataBinding.backLayout.title = "카테고리 편집"
        viewDataBinding.backLayout.backBtnOnClickListener = setOnBackBtnClickListener()
        viewDataBinding.fragment = this
        setCategoryList()
    }

    private fun setCategoryList() {

        bucketInfoViewModel.getCategoryList(object : BucketInfoViewModel.GetBucketListCallBackListener {
            override fun success(categoryList: List<Category>) {

                editCategoryListAdapter = EditCategoryListAdapter(categoryList as MutableList<Category>, this@CategoryEditFragment, this@CategoryEditFragment, this@CategoryEditFragment)

                viewDataBinding.categoryListRecyclerView.apply {
                    adapter = editCategoryListAdapter
                    layoutManager = LinearLayoutManager(context)
                }

                itemTouchHelper = ItemTouchHelper(CategoryItemTouchHelperCallback(editCategoryListAdapter))
                itemTouchHelper.attachToRecyclerView(viewDataBinding.categoryListRecyclerView)
            }

            override fun start() {
                startLoading()
            }


            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(activity!!.supportFragmentManager)

            }

        }, getAccessToken(context!!), getUserId(context!!))

    }

    public fun setCategoryDelectListener() {

        categoryEditViewModel.removeCategoryItem(removedList, object : CategoryInfoViewModel.ChangeCategoryState {
            override fun start() {

            }

            override fun success() {

            }

            override fun fail() {

            }

        })

        Log.e("ayhan", "setCategoryDelectListener")

    }


    override fun setOnBackBtnClickListener(): View.OnClickListener {
        for (list in changeCategoryList) {
            Log.e("ayhan", "변경순서 : ${list.name}")
        }

        return super.setOnBackBtnClickListener()

    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun checked(isChecked: Boolean, item: Category) {
        if (isChecked) {
            removedList.add(item.id)
        } else {
            removedList.remove(item.id)
        }

        viewDataBinding.cancelText.isEnabled = removedList.size > 0

        for (i in removedList) {
            Log.e("ayhan", "itemId : ${i}")
        }

    }

    override fun movend(list: List<Category>) {
        changeCategoryList = list as ArrayList<Category>
    }


}