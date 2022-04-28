package womenproject.com.mybury.presentation.main.bucketlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.databinding.ItemBucketSucceedBinding

class SucceedBucketItemViewHolder(private val binding: ItemBucketSucceedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(bucketItemHandler: BucketItemHandler, bucketItemInfo: BucketItem) {
        binding.apply {

            setUI(bucketItemHandler, bucketItemInfo)
            executePendingBindings()
        }
    }

    private fun setUI(bucketItemHandler: BucketItemHandler, bucketItemInfo: BucketItem) {
        binding.apply {
            ddayTextView.visibility =
                if (Preference.getShowDdayFilter(binding.root.context)) View.VISIBLE else View.GONE
            bucketTitleText = bucketItemInfo.title
            bucketClickListener = View.OnClickListener {
                bucketItemHandler.bucketSelect(bucketItemInfo)
            }
        }
    }
}