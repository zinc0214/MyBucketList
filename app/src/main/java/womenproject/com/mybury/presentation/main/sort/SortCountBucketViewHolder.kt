package womenproject.com.mybury.presentation.main.sort

import android.annotation.SuppressLint
import android.text.Html
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.ItemSortCountBucketBinding
import womenproject.com.mybury.ui.ItemDragListener

class SortCountBucketViewHolder(
    private val binding: ItemSortCountBucketBinding,
    private val dragListener: ItemDragListener
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ClickableViewAccessibility")
    fun bind(bucketItem: BucketItem) {
        binding.apply {
            bucketInfo = bucketItem

            editImageView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this@SortCountBucketViewHolder)
                }
                false
            }

            userCount.text = Html.fromHtml(
                String.format(
                    root.context.resources.getString(R.string.bucket_count),
                    "${bucketItem.userCount}", "/${bucketItem.goalCount}"
                ),
                0
            )
            executePendingBindings()
        }
    }
}