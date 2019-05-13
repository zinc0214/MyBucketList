package womenproject.com.mybury.viewholder

import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.CategoryList
import womenproject.com.mybury.databinding.CategoryListItemBinding
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.viewmodels.CategoryListItemViewModel

class EditCategoryListViewHolder(private val binding: CategoryListItemBinding, private val listener: ItemDragListener)
    : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.dragLayout.setOnTouchListener { v, event ->
            Log.e("ayhan", "isDrag")
            if (event.action == MotionEvent.ACTION_DOWN) {
                listener.onStartDrag(this)
            }
            false
        }
    }

    fun bind(categoryName: String) {
        binding.apply {

            binding.viewModel = CategoryListItemViewModel(categoryName)

            executePendingBindings()
        }
    }
}