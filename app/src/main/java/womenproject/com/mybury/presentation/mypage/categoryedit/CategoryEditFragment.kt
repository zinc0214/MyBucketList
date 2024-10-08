package womenproject.com.mybury.presentation.mypage.categoryedit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.databinding.FragmentCategoryEditBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.dialog.LoadFailDialog
import womenproject.com.mybury.presentation.viewmodels.CategoryViewModel
import womenproject.com.mybury.ui.ItemCheckedListener
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.ui.ItemMovedListener
import womenproject.com.mybury.util.observeNonNull
import womenproject.com.mybury.util.showToast

@AndroidEntryPoint
class CategoryEditFragment : BaseFragment(),
    ItemDragListener,
    ItemCheckedListener,
    ItemMovedListener {

    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var binding: FragmentCategoryEditBinding

    private val categoryViewModel by viewModels<CategoryViewModel>()

    private val removedList = hashSetOf<String>()
    private var changeCategoryList = arrayListOf<Category>()
    private var originCategoryList = arrayListOf<Category>()

    private lateinit var imm: InputMethodManager
    private var isKeyBoardShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val goToActionCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isCancelConfirm) {
                    isEnabled = false
                    requireActivity().onBackPressed()
                } else {
                    actionByBackButton()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, goToActionCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category_edit, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataBinding()
        setUpViewModelObservers()
    }

    private fun initDataBinding() {
        isCancelConfirm = false

        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.backLayout.title = "카테고리 편집"
        binding.backLayout.setBackBtnOnClickListener { actionByBackButton() }
        binding.fragment = this

        categoryViewModel.loadCategoryList()
    }

    private fun setUpViewModelObservers() {
        categoryViewModel.categoryLoadState.observeNonNull(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> {
                    startLoading()
                }
                LoadState.RESTART -> {
                    categoryViewModel.loadCategoryList()
                }
                LoadState.SUCCESS -> {
                    stopLoading()
                }
                LoadState.FAIL -> {
                    stopLoading()
                    LoadFailDialog {
                        backBtnOnClickListener()
                    }.show(requireActivity().supportFragmentManager, "tag")
                }
            }
        }

        categoryViewModel.addCategoryItemState.observeNonNull(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> startLoading()

                LoadState.SUCCESS -> {
                    stopLoading()
                    initDataBinding()
                    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                }
                LoadState.RESTART, LoadState.FAIL -> {
                    stopLoading()
                    context?.showToast("카테고리 추가에 실패했습니다.\n다시 시도해주세요.")
                }

            }
        }

        categoryViewModel.categoryList.observe(viewLifecycleOwner) {
            initOriginCategory(it as List<Category>)
            changeCategoryList = it as ArrayList<Category>
            setCategoryAdapter()
            isKeyBoardShown = false
        }

        categoryViewModel.editCategoryItemNameState.observeNonNull(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> startLoading()
                LoadState.SUCCESS -> {
                    stopLoading()
                    initDataBinding()
                }
                LoadState.FAIL -> {
                    stopLoading()
                    context?.showToast("카테고리 이름 수정에 실패했습니다.\n다시 시도해주세요.")
                }
                else -> {
                    // Do Nothing
                }
            }
        }

        categoryViewModel.changeCategoryItemState.observeNonNull(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> {
                    startLoading()
                }
                LoadState.RESTART -> {
                    setCategoryStatusChange()
                    stopLoading()
                }
                LoadState.SUCCESS -> {
                    context?.showToast("카테고리 순서가 변경되었습니다.")
                    stopLoading()
                    onBackPressedFragment()
                }
                LoadState.FAIL -> {
                    context?.showToast("카테고리 순서 변경에 실패했습니다.\n 다시 시도해주세요.")
                    stopLoading()
                }
            }
        }

        categoryViewModel.removeCategoryItemState.observeNonNull(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> startLoading()
                LoadState.RESTART -> removeCategoryItem()
                LoadState.SUCCESS -> {
                    context?.showToast("카테고리가 삭제되었습니다.")
                    stopLoading()
                    initDataBinding()
                    removedList.clear()
                    binding.cancelText.isEnabled = false
                }
                LoadState.FAIL -> {
                    context?.showToast("카테고리 삭제에 실패했습니다.\n 다시 시도해주세요.")
                    stopLoading()
                }
            }
        }
    }

    private fun initOriginCategory(categoryList: List<Category>) {
        categoryList.forEach {
            originCategoryList.add(it)
        }
    }

    private fun setCategoryAdapter() {

        val editCategoryName: (Category) -> Unit = {
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
            val categoryAdd: (String) -> Unit = { name ->
                editCategoryItem(it, name)
            }
            AddCategoryDialogFragment(originCategoryList, it.name, categoryAdd)
                .show(requireActivity().supportFragmentManager)
        }

        val editCategoryListAdapter = EditCategoryListAdapter(
            changeCategoryList,
            this@CategoryEditFragment,
            this@CategoryEditFragment,
            this@CategoryEditFragment,
            editCategoryName
        )

        binding.categoryListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = editCategoryListAdapter
        }

        itemTouchHelper = ItemTouchHelper(CategoryItemTouchHelperCallback(editCategoryListAdapter))
        itemTouchHelper.attachToRecyclerView(binding.categoryListRecyclerView)
    }

    fun addNewCategoryListener() {
        val categoryAdd: (String) -> Unit = {
            addNewCategory(it)
        }
        AddCategoryDialogFragment(
            originCategoryList,
            null,
            categoryAdd
        ).show(requireActivity().supportFragmentManager)
    }

    private fun editCategoryItem(category: Category, newName: String) {
        categoryViewModel.editCategoryItem(category, newName)
    }

    private fun addNewCategory(name: String) {
        categoryViewModel.addCategoryItem(name)
    }

    fun removeCategoryItem() {
        categoryViewModel.removeCategoryItem(removedList)
    }

    private fun setCategoryStatusChange() {
        categoryViewModel.changeCategoryStatus(
            changeCategoryList
        )
    }

    override fun actionByBackButton() {
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        if (originCategoryList == changeCategoryList) {
            onBackPressedFragment()
        } else {
            setCategoryStatusChange()
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun checked(isChecked: Boolean, item: Any) {
        val category = item as Category
        if (isChecked) {
            removedList.add(category.id)
        } else {
            removedList.remove(category.id)
        }

        binding.cancelText.isEnabled = removedList.size > 0
    }

    override fun moved(list: List<Any>) {
        changeCategoryList = list as ArrayList<Category>
    }
}