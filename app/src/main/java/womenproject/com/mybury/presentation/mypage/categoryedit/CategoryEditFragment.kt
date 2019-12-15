package womenproject.com.mybury.presentation.mypage.categoryedit

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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

    private lateinit var itemTouchHelper: ItemTouchHelper

    private val bucketInfoViewModel = BucketInfoViewModel()
    private val categoryEditViewModel = CategoryInfoViewModel()
    private val removedList = hashSetOf<String>()
    private var changeCategoryList = arrayListOf<Category>()

    private lateinit var imm : InputMethodManager
    private var isKeyBoardShown = false

    override val layoutResourceId: Int
        get() = R.layout.fragment_category_edit

    override val viewModel: MyPageViewModel
        get() = MyPageViewModel()


    override fun initDataBinding() {
        imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        viewDataBinding.backLayout.title = "카테고리 편집"
        viewDataBinding.backLayout.backBtnOnClickListener = setOnBackBtnClickListener()
        viewDataBinding.fragment = this
        viewDataBinding.addCategoryItem.categoryItemLayout.visibility = View.GONE
        viewDataBinding.root.viewTreeObserver.addOnGlobalLayoutListener(setOnSoftKeyboardChangedListener())
        setCategoryList()
    }

    private fun setCategoryList() {

        bucketInfoViewModel.getCategoryList(object : BucketInfoViewModel.GetBucketListCallBackListener {
            override fun success(categoryList: List<Category>) {

                val editCategoryName : (Category, String) -> Unit = { category : Category , newName : String ->
                    Log.e("ayhan", "pre : ${category.name}, chan : ${newName}")
                    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                    categoryEditViewModel.editCategoryItem(getAccessToken(context!!), newName, object : CategoryInfoViewModel.ChangeCategoryState {
                        override fun start() {
                            startLoading()
                        }

                        override fun success() {
                            stopLoading()
                            setCategoryList()
                        }

                        override fun fail() {
                            stopLoading()
                        }

                    })
                }

                val editCategoryListAdapter = EditCategoryListAdapter(categoryList as MutableList<Category>,
                        this@CategoryEditFragment,
                        this@CategoryEditFragment,
                        this@CategoryEditFragment,
                        editCategoryName)

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

    fun addNewCategoryListener() {
        viewDataBinding.addCategoryItem.categoryItemLayout.visibility = View.VISIBLE
        viewDataBinding.addCategoryItem.categoryText.setOnEditorActionListener { v, actionId, event ->
            when(actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if(v?.text.isNullOrEmpty()) {
                        viewDataBinding.addCategoryItem.categoryItemLayout.visibility = View.GONE
                        viewDataBinding.addCategoryItem.categoryText.text.clear()
                    } else{
                        categoryEditViewModel.addCategoryItem(getAccessToken(context!!), v!!.text.toString(), object : CategoryInfoViewModel.ChangeCategoryState {
                            override fun start() {
                                startLoading()
                            }

                            override fun success() {
                                stopLoading()
                                setCategoryList()
                                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                                viewDataBinding.addCategoryItem.categoryItemLayout.visibility= View.GONE
                                viewDataBinding.addCategoryItem.categoryText.text.clear()
                            }

                            override fun fail() {
                                stopLoading()
                            }

                        })
                    }

                }
            }
            true
        }
    }

    fun setCategoryDeleteListener() {

        categoryEditViewModel.removeCategoryItem(removedList, object : CategoryInfoViewModel.ChangeCategoryState {
            override fun start() {

            }

            override fun success() {

            }

            override fun fail() {

            }

        })

        Log.e("ayhan", "setCategoryDeleteListener")

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

        viewDataBinding.cancelText.isEnabled = removedList.size > 1

        for (i in removedList) {
            Log.e("ayhan", "itemId : ${i}")
        }

    }

    override fun movend(list: List<Category>) {
        changeCategoryList = list as ArrayList<Category>
    }

    private fun setOnSoftKeyboardChangedListener(): ViewTreeObserver.OnGlobalLayoutListener {
        return ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            viewDataBinding.root.getWindowVisibleDisplayFrame(r)

            val heightDiff = viewDataBinding.root.rootView.height - (r.bottom - r.top)
            Log.e("ayhan", "${heightDiff}")
            try {
                if (heightDiff < 500) {
                    if(viewDataBinding.addCategoryItem.categoryItemLayout.visibility == View.VISIBLE && isKeyBoardShown) {
                        viewDataBinding.addCategoryItem.categoryItemLayout.visibility = View.GONE
                        viewDataBinding.addCategoryItem.categoryText.text.clear()
                        isKeyBoardShown = false
                    }
                } else {
                    isKeyBoardShown = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}