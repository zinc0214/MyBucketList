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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.addOnBackPressedCallback(this, OnBackPressedCallback {
            if (isCancelConfirm) {
                false
            } else {
                actionByBackButton()
                true
            }

        })
    }

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
                originCategoryList = value as ArrayList<Category>
                changeCategoryList = value

                val editCategoryName: (Category, String) -> Unit = { category: Category, newName: String ->
                    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                    editCategoryItem(category, newName)
                    viewDataBinding.bottomLayout.visibility = View.VISIBLE
                }
                val editCategoryListAdapter = EditCategoryListAdapter(value as MutableList<Category>,
                        this@CategoryEditFragment,
                        this@CategoryEditFragment,
                        this@CategoryEditFragment,
                        editCategoryName)

                viewDataBinding.categoryListRecyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = editCategoryListAdapter
                }

                itemTouchHelper = ItemTouchHelper(CategoryItemTouchHelperCallback(editCategoryListAdapter))
                itemTouchHelper.attachToRecyclerView(viewDataBinding.categoryListRecyclerView)

                isKeyBoardShown = false
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
        Log.e("ayhan", "addNewCategoryListener is go")
        viewDataBinding.addCategoryItem.categoryItemLayout.visibility = View.VISIBLE
        viewDataBinding.categoryListRecyclerView.scrollToPosition(originCategoryList.lastIndex)
        viewDataBinding.addCategoryItem.categoryText.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (v?.text.isNullOrBlank()) {
                        viewDataBinding.addCategoryItem.categoryItemLayout.visibility = View.GONE
                        viewDataBinding.addCategoryItem.categoryText.text.clear()
                        viewDataBinding.bottomLayout.visibility = View.VISIBLE
                    } else {
                        if(alreadyUseName(v.text.toString())) {
                            Toast.makeText(context, "동일한 카테고리 이름이 존재합니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            addNewCategory(v.text.toString())
                        }
                    }
                }
            }
            true
        }
    }

    private fun alreadyUseName(newCategory: String) : Boolean {

        originCategoryList.forEach{
            if(it.name == newCategory) return true
        }
        return false
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
                originCategoryList = changeCategoryList
                viewDataBinding.bottomLayout.visibility = View.VISIBLE
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
                viewDataBinding.bottomLayout.visibility = View.VISIBLE
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
                viewDataBinding.cancelText.isEnabled = false
            }

            override fun fail() {
                Toast.makeText(context, "카테고리 삭제에 실패했습니다.\n 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                stopLoading()
            }

        })
    }

    fun setCategoryStatusChange() {
        categoryEditViewModel.changeCategoryStatus(changeCategoryList, object : BaseViewModel.Simple3CallBack {
            override fun start() {
                startLoading()
            }

            override fun success() {
                Toast.makeText(context, "카테고리 순서가 변경되었습니다.", Toast.LENGTH_SHORT).show()
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
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        viewDataBinding.bottomLayout.visibility = View.VISIBLE
        if (changeCategoryList == originCategoryList) {
            Log.e("ayhan", "why...........")
            onBackPressedFragment()
        } else {
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
    }

    override fun movend(list: List<Category>) {
        Log.e("ayhan", "move")
        changeCategoryList = list as ArrayList<Category>
    }

    private fun setOnSoftKeyboardChangedListener(): ViewTreeObserver.OnGlobalLayoutListener {
        return ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            viewDataBinding.root.getWindowVisibleDisplayFrame(r)

            val heightDiff = viewDataBinding.root.rootView.height - (r.bottom - r.top)
            Log.e("ayhan", "heigth Deiif : ${heightDiff}")
            try {
                if (heightDiff < 300) {
                    if (viewDataBinding.addCategoryItem.categoryItemLayout.visibility == View.VISIBLE && isKeyBoardShown) {
                        viewDataBinding.addCategoryItem.categoryItemLayout.visibility = View.GONE
                        viewDataBinding.addCategoryItem.categoryText.text.clear()
                        isKeyBoardShown = false
                    }
                    viewDataBinding.bottomLayout.visibility = View.VISIBLE
                    viewDataBinding.space.visibility = View.VISIBLE
                } else {
                    isKeyBoardShown = true
                    viewDataBinding.bottomLayout.visibility = View.GONE
                    viewDataBinding.space.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}