package womenproject.com.mybury.presentation.main.bucketlist

import android.content.Context
import android.view.View
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.BucketItemSucceedBinding

class SucceedBucketItemViewHolder(private val binding: BucketItemSucceedBinding) : BaseBucketItemViewHolder(binding) {

    override fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem, context: Context) {
        binding.apply {

            setUI(bucketItemInfo, bucketListener)
            executePendingBindings()
        }
    }

    override fun setUI(bucketItemInfo: BucketItem, bucketListener: View.OnClickListener) {
        super.setUI(bucketItemInfo, bucketListener)

        binding.bucketTitleText = bucketItemInfo.title
        binding.bucketClickListener = bucketListener
    }


}