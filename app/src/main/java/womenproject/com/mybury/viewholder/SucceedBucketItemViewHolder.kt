package womenproject.com.mybury.viewholder

import android.content.Context
import android.view.View
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.BucketItemSucceedBinding

class SucceedBucketItemViewHolder(private val binding: BucketItemSucceedBinding) : BaseBucketItemViewHolder(binding) {

    override fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem, context: Context) {
        binding.apply {
            bucketTitleText = bucketItemInfo.title
            bucketClickListener = bucketListener
            compelete = bucketItemInfo.complete
            isLastItem = bucketItemInfo.isLast
            bucketClickListener = bucketListener
            bucketTitleText = bucketItemInfo.title

            lastImgVisible = if(isLastItem) {
                View.VISIBLE
            } else {
                View.GONE
            }
            executePendingBindings()
        }
    }
}