package womenproject.com.mybury.presentation.mypage.dday

import android.view.View
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.DdayBucketItemSucceedBinding
import womenproject.com.mybury.presentation.main.bucketlist.BaseBucketItemViewHolder

class DdaySucceedBucketItemViewHolder(private val binding: DdayBucketItemSucceedBinding) : BaseBucketItemViewHolder(binding) {

    override fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem) {
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