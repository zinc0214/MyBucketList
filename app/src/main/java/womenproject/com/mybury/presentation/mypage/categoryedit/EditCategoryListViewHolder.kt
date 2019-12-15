package womenproject.com.mybury.presentation.mypage.categoryedit

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.databinding.CategoryListItemBinding
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.presentation.viewmodels.CategoryListItemViewModel
import womenproject.com.mybury.ui.ItemCheckedListener

class EditCategoryListViewHolder(private val binding: CategoryListItemBinding,
                                 private val dragListener: ItemDragListener,
                                 private val checkedListener: ItemCheckedListener,
                                 private val editCategoryName : (Category, String) -> Unit)
    : RecyclerView.ViewHolder(binding.root) {

    private var isKeyBoardShown = false

    @SuppressLint("ClickableViewAccessibility")
    fun bind(category: Category) {
        binding.apply {

            viewModel = CategoryListItemViewModel(category.name)

            if (category.name == "없음") {
                removeBox.isEnabled = false
                removeBox.isChecked = false
            }
            dragLayout.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this@EditCategoryListViewHolder)
                }
                false
            }

            removeBox.setOnCheckedChangeListener { _, isChecked ->
                checkedListener.checked(isChecked, category)
            }

            editCategoryNameListener(category)
            binding.root.viewTreeObserver.addOnGlobalLayoutListener(setOnSoftKeyboardChangedListener(category.name))
            executePendingBindings()
        }
    }


    private fun editCategoryNameListener(category: Category) {
        binding.categoryText.setOnEditorActionListener { v, actionId, event ->
            when(actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if(v?.text.isNullOrEmpty()) {
                        binding.categoryText.setText(category.priority)
                    } else{
                        editCategoryName.invoke(category, v.text.toString())
                    }
                }
            }
            true
        }
    }
    private fun setOnSoftKeyboardChangedListener(name : String): ViewTreeObserver.OnGlobalLayoutListener {
        return ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            binding.root.getWindowVisibleDisplayFrame(r)

            val heightDiff = binding.root.rootView.height - (r.bottom - r.top)
            Log.e("ayhan222", "${heightDiff}")
            try {
                if (heightDiff < 500) {
                    binding.categoryText.setText(name)
                    isKeyBoardShown = false
                } else {
                    isKeyBoardShown = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}