package womenproject.com.mybury.viewholder

import android.content.Context
import android.view.View
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.BucketItemBaseBinding

/**
 * Created by HanAYeon on 2019. 1. 9..
 */

class NoneCountBucketItemViewHolder(private val binding: BucketItemBaseBinding) : BaseBucketItemViewHolder(binding) {

    override fun bind(bucketListener: View.OnClickListener, bucektItemInfo : BucketItem, context : Context) {
        binding.apply {
            if(bucektItemInfo.dday == 1) { bucketItemLayout.background = context.getDrawable(R.drawable.bucket_dday_item_background)}
            bucketClickListener = bucketListener
            bucket = bucektItemInfo.title
            executePendingBindings()
        }
    }
}