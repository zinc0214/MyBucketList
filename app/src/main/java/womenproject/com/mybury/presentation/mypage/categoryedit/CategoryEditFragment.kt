package womenproject.com.mybury.presentation.mypage.categoryedit

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.databinding.FragmentCategoryEditBinding
import womenproject.com.mybury.presentation.NetworkFailDialog
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel
import womenproject.com.mybury.presentation.viewmodels.CategoryInfoViewModel
import womenproject.com.mybury.presentation.viewmodels.MyPageViewModel
import womenproject.com.mybury.ui.ItemCheckedListener
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.ui.ItemMovedListener


class CategoryEditFragment : BaseFragment<FragmentCategoryEditBinding, MyPageViewModel>(),
        ItemDragListener,
        ItemCheckedListener,
        ItemMovedListener {

    private lateinit var itemTouchHelper: ItemTouchHelper

    private val bucketInfoViewModel = BucketInfoViewModel()
    private val categoryEditViewModel = CategoryInfoViewModel()
    private val removedList = hashSetOf<String>()
    private var changeCategoryList = arrayListOf<Category>()
    private var originCategoryList = arrayListOf<Category>()

    private lateinit var imm: InputMethodManager
    private var isKeyBoardShown = false

    override val layoutResourceId: Int
        get() = R.layout.fragment_category_edit

    override val viewModel: MyPageViewModel
        get() = MyPageViewModel()


    override fun initDataBinding() {
        imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        viewDataBinding.backLayout.title = "카테고리 편집"
        viewDataBinding.backLayout.setBackBtnOnClickListener { _ -> actionByBackButton() }
        viewDataBinding.fragment = this
        viewDataBinding.addCategoryItem.categoryItemLayout.visibility = View.GONE
        viewDataBinding.root.viewTreeObserver.addOnGlobalLayoutListener(setOnSoftKeyboardChangedListener())
        setCategoryList()
    }

    private fun setCategoryList() {

        bucketInfoViewModel.getCategoryList(object : BaseViewModel.MoreCallBackAnyList {
            override fun restart() {
                setCategoryList()
            }

            override fun success(value: List<Any>) {
                val editCategoryName: (Category, String) -> Unit = { category: Category, newName: String ->
                    Log.e("ayhan", "pre : ${category.name}, chan : ${newName}")
                    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                    editCategoryItem(category, newName)
                    originCategoryList = value as ArrayList<Category>
                    changeCategoryList = value as ArrayList<Category>
                }

                Log.e("ayhan", "bubusccess : ${value.size}")
                val editCategoryListAdapter = EditCategoryListAdapter(value as MutableList<Category>,
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

        })

    }

    fun addNewCategoryListener() {
        viewDataBinding.addCategoryItem.categoryItemLayout.visibility = View.VISIBLE
        viewDataBinding.addCategoryItem.categoryText.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (v?.text.isNullOrBlank()) {
                        viewDataBinding.addCategoryItem.categoryItemLayout.visibility = View.GONE
                        viewDataBinding.addCategoryItem.categoryText.text.clear()
                    } else {
                        addNewCategory(v!!.text.toString())
                    }

                }
            }
            true
        }
    }

    private fun editCategoryItem(category: Category, newName: String) {
        categoryEditViewModel.editCategoryItem(category, newName, object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                editCategoryItem(category, newName)
            }

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

    private fun addNewCategory(name: String) {
        categoryEditViewModel.addCategoryItem(name, object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                addNewCategory(name)
            }

            override fun start() {
                startLoading()
            }

            override fun success() {
                stopLoading()
                setCategoryList()
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                viewDataBinding.addCategoryItem.categoryItemLayout.visibility = View.GONE
                viewDataBinding.addCategoryItem.categoryText.text.clear()
            }

            override fun fail() {
                stopLoading()
            }

        })
    }

    fun setCategoryDeleteListener() {

        categoryEditViewModel.removeCategoryItem(removedList, object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                setCategoryDeleteListener()
            }

            override fun start() {
                startLoading()
            }

            override fun success() {
                Toast.makeText(context, "카테고리가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                stopLoading()
                setCategoryList()
                removedList.clear()
            }

            override fun fail() {
                Toast.makeText(context, "카테고리 삭제에 실패했습니다.\n 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                stopLoading()
            }

        })

        Log.e("ayhan", "setCategoryDeleteListener")

    }

    fun setCategoryStatusChange() {
        categoryEditViewModel.changeCategoryStatus(changeCategoryList, object : BaseViewModel.Simple3CallBack {
            override fun start() {
                startLoading()
            }

            override fun success() {
                Toast.makeText(context, "카테고리 순서가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                Log.e("ayhan", "is Change")
                stopLoading()
                onBackPressedFragment()
            }

            override fun fail() {
                Toast.makeText(context, "카테고리 순서 변경에 실패했습니다.\n 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                stopLoading()
            }

            override fun restart() {
                setCategoryStatusChange()
                stopLoading()
            }

        })
    }

    override fun actionByBackButton() {
        if (changeCategoryList == originCategoryList) {
            Log.e("ayhan", "SAME")
            onBackPressedFragment()
        } else {
            Log.e("ayhan", "NOTSAME")
            setCategoryStatusChange()
        }
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

    private fun setOnSoftKeyboardChangedListener(): ViewTreeObserver.OnGlobalLayoutListener {
        return ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            viewDataBinding.root.getWindowVisibleDisplayFrame(r)

            val heightDiff = viewDataBinding.root.rootView.height - (r.bottom - r.top)
            Log.e("ayhan", "${heightDiff}")
            try {
                if (heightDiff < 500) {
                    if (viewDataBinding.addCategoryItem.categoryItemLayout.visibility == View.VISIBLE && isKeyBoardShown) {
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