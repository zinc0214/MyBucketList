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
import io.reactivex.rxkotlin.toObservable
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
        isCancelConfirm = false

        imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        viewDataBinding.backLayout.title = "카테고리 편집"
        viewDataBinding.backLayout.setBackBtnOnClickListener { _ -> actionByBackButton() }
        viewDataBinding.fragment = this
        viewDataBinding.root.viewTreeObserver.addOnGlobalLayoutListener(setOnSoftKeyboardChangedListener())
        setCategoryList()
    }

    private fun setCategoryList() {

        bucketInfoViewModel.getCategoryList(object : BaseViewModel.MoreCallBackAnyList {
            override fun restart() {
                setCategoryList()
            }

            override fun success(value: List<Any>) {
                Log.e("ayhan", "setCategoryList")
                initOriginCategory(value as List<Category>)
                changeCategoryList = value as ArrayList<Category>
                setCategoryAdapter()
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

    private fun initOriginCategory(categoryList : List<Category>) {
        categoryList.forEach{
            originCategoryList.add(it)
        }
    }
    private fun setCategoryAdapter() {

        val editCategoryName: (Category) -> Unit = {
            imm.hideSoftInputFromWindow(view!!.windowToken, 0)

            if(it.name == "없음") {
                Toast.makeText(context, "기본 카테고리 이름은 변경할 수 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val categoryAdd: (String) -> Unit = { name ->
                    editCategoryItem(it, name)
                }
                AddCategoryDialogFragment(originCategoryList, it.name, categoryAdd)
                        .show(activity!!.supportFragmentManager)
            }
        }

        val editCategoryListAdapter = EditCategoryListAdapter(changeCategoryList,
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
    }

    fun addNewCategoryListener() {
        val categoryAdd: (String) -> Unit = {
            addNewCategory(it)
        }
        AddCategoryDialogFragment(originCategoryList, null, categoryAdd).show(activity!!.supportFragmentManager)
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
                initDataBinding()
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
                initDataBinding()
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
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
                initDataBinding()
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
        if (originCategoryList == changeCategoryList) {
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

    override fun moved(list: List<Category>) {
        Log.e("ayhan", "move, ${list[0].name}")
        changeCategoryList = list as ArrayList<Category>
        Log.e("ayhan", "moveq, ${changeCategoryList[0].name}")
        Log.e("ayhan", "movew, ${originCategoryList[0].name}")
    }

    private fun setOnSoftKeyboardChangedListener(): ViewTreeObserver.OnGlobalLayoutListener {
        return ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            viewDataBinding.root.getWindowVisibleDisplayFrame(r)

            val heightDiff = viewDataBinding.root.rootView.height - (r.bottom - r.top)
            try {
                if (heightDiff < 300) {
                    viewDataBinding.space.visibility = View.VISIBLE
                } else {
                    isKeyBoardShown = true
                    viewDataBinding.space.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}