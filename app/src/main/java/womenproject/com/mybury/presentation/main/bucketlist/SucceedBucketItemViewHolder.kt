package womenproject.com.mybury.presentation.main.bucketlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.databinding.ItemBucketSucceedBinding

class SucceedBucketItemViewHolder(private val binding: ItemBucketSucceedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem) {
        binding.apply {

            setUI(bucketItemInfo, bucketListener)
            executePendingBindings()
        }
    }

    private fun setUI(bucketItemInfo: BucketItem, bucketListener: View.OnClickListener) {
        binding.apply {
            ddayTextView.visibility =
                if (Preference.getShowDdayFilter(binding.root.context)) View.VISIBLE else View.GONE
            bucketTitleText = bucketItemInfo.title
            bucketClickListener = bucketListener
        }
    }
}