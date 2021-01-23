package womenproject.com.mybury.presentation.mypage.categoryedit

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.databinding.ItemCategoryBinding
import womenproject.com.mybury.presentation.viewmodels.CategoryListItemViewModel
import womenproject.com.mybury.ui.ItemCheckedListener
import womenproject.com.mybury.ui.ItemDragListener

class EditCategoryListViewHolder(private val binding: ItemCategoryBinding,
                                 private val dragListener: ItemDragListener,
                                 private val checkedListener: ItemCheckedListener,
                                 private val editCategoryName: (Category) -> Unit)
    : RecyclerView.ViewHolder(binding.root) {

    private var isKeyBoardShown = false

    @SuppressLint("ClickableViewAccessibility")
    fun bind(category: Category) {
        binding.apply {

            if(category.name != null) {
                viewModel = CategoryListItemViewModel(category.name)
            }
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
            editLayout.setOnClickListener { v ->
                editCategoryName.invoke(category)
            }
            removeBox.setOnCheckedChangeListener { _, isChecked ->
                checkedListener.checked(isChecked, category)
            }

            binding.root.viewTreeObserver.addOnGlobalLayoutListener(setOnSoftKeyboardChangedListener(category.name))
            executePendingBindings()
        }
    }

    private fun setOnSoftKeyboardChangedListener(name : String): ViewTreeObserver.OnGlobalLayoutListener {
        return ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            binding.root.getWindowVisibleDisplayFrame(r)

            val heightDiff = binding.root.rootView.height - (r.bottom - r.top)
            try {
                isKeyBoardShown = if (heightDiff < 500) {
                    binding.categoryText.setText(name)
                    false
                } else {
                    true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}