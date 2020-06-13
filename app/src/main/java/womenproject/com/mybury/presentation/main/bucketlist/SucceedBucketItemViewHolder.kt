package womenproject.com.mybury.presentation.main.bucketlist

import android.content.Context
import android.view.View
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.databinding.BucketItemSucceedBinding

class SucceedBucketItemViewHolder(private val binding: BucketItemSucceedBinding) : BaseBucketItemViewHolder(binding) {

    override fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem) {
        binding.apply {

            setUI(bucketItemInfo, bucketListener)
            executePendingBindings()
        }
    }

    override fun setUI(bucketItemInfo: BucketItem, bucketListener: View.OnClickListener) {
        super.setUI(bucketItemInfo, bucketListener)

        binding.apply {
            ddayTextView.visibility = if(Preference.getShowDdayFilter(binding.root.context)) View.VISIBLE else  View.GONE
            bucketTitleText = bucketItemInfo.title
            bucketClickListener = bucketListener
        }
    }
}