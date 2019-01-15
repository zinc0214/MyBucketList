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
            executePendingBindings()
        }
    }


    override fun setFinalSuccessUIButton() { }

    override fun setFinalSuccessUIBackground() { }

    override fun setFinalSucceedUIBackground() { }

    override fun setDoneSuccessUIButton() { }

    override fun addCurrentValue() { }
}