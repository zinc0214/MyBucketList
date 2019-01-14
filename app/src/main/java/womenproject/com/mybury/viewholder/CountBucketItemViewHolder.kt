package womenproject.com.mybury.viewholder

import android.content.Context
import android.view.View
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.BucketItemCountBinding

/**
 * Created by HanAYeon on 2019. 1. 9..
 */

class CountBucketItemViewHolder(private val binding: BucketItemCountBinding) : BaseBucketItemViewHolder(binding) {

    private var previousValue = 0

    override fun bind(bucketListener: View.OnClickListener, bucektItemInfo: BucketItem, context: Context) {
        binding.apply {
            if (bucektItemInfo.dday == 1) {
                bucketItemLayout.background = context.getDrawable(R.drawable.bucket_dday_item_background)
            }
            previousValue = bucektItemInfo.count
            bucket = bucektItemInfo.title
            currentValue = bucektItemInfo.count.toString()
            horizontalProgressBar.progress = bucektItemInfo.count
            bucketClickListener = bucketListener
            bucketSuccessListener = bucektSuccessListener()
            executePendingBindings()
        }
    }

    private fun bucektSuccessListener(): View.OnClickListener {
        return View.OnClickListener {
            binding.currentValue = (previousValue + 1).toString()
            binding.horizontalProgressBar.progress = previousValue + 1
            previousValue = previousValue + 1

        }
    }
}