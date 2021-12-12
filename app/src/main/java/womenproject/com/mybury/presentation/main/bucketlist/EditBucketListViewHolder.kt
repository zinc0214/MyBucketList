package womenproject.com.mybury.presentation.main.bucketlist

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.ItemEditBucketBinding
import womenproject.com.mybury.ui.ItemDragListener

class EditBucketListViewHolder(
    private val binding: ItemEditBucketBinding,
    private val dragListener: ItemDragListener
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ClickableViewAccessibility")
    fun bind(bucketItem: BucketItem) {
        binding.apply {
            bucketTitleText = bucketItem.title

            editImageView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this@EditBucketListViewHolder)
                }
                false
            }

            executePendingBindings()
        }
    }
}