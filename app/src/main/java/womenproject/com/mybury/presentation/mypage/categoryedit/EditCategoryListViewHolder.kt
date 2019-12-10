package womenproject.com.mybury.presentation.mypage.categoryedit

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.databinding.CategoryListItemBinding
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.presentation.viewmodels.CategoryListItemViewModel
import womenproject.com.mybury.ui.ItemCheckedListener

class EditCategoryListViewHolder(private val binding: CategoryListItemBinding, private val dragListener: ItemDragListener, private val checkedListener: ItemCheckedListener)
    : RecyclerView.ViewHolder(binding.root) {


    @SuppressLint("ClickableViewAccessibility")
    fun bind(category : Category) {
        binding.apply {

            viewModel = CategoryListItemViewModel(category.name)

            if(category.name == "없음") {
                removeBox.isEnabled = false
            }
            dragLayout.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this@EditCategoryListViewHolder)
                }
                false
            }

            removeBox.setOnCheckedChangeListener { _, isChecked ->
                checkedListener.checked(isChecked, category)
            }


            executePendingBindings()
        }
    }
}