package womenproject.com.mybury.presentation.main.sort

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.ItemSortBaseBucketBinding
import womenproject.com.mybury.ui.ItemDragListener

class SortBaseBucketViewHolder(
    private val binding: ItemSortBaseBucketBinding,
    private val dragListener: ItemDragListener
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ClickableViewAccessibility")
    fun bind(bucketItem: BucketItem) {
        binding.apply {
            bucketTitleText = bucketItem.title

            editImageView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this@SortBaseBucketViewHolder)
                }
                false
            }

            executePendingBindings()
        }
    }
}