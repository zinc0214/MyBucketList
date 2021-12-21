package womenproject.com.mybury.presentation.main.bucketlist

import android.view.View
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.databinding.ItemBucketDoingSimpleBinding

/**
 * Created by HanAYeon on 2019. 1. 9..
 */

open class BaseNormalBucketItemViewHolder(private val binding: ItemBucketDoingSimpleBinding,
                                          private val showSnackBar: ((BucketItem) -> Unit))
    : BaseBucketItemViewHolder(binding, showSnackBar) {

    init {
        bucketItemLayout = binding.bucketItemLayout
        successImageView = binding.successButtonLayout.successImg
        bucketItemImage = binding.bucketItemImage
        bucketTitle = binding.bucketTitle
        circularProgressBar = binding.successButtonLayout.circularProgressBar
        ddayCountView = binding.ddayTextView
    }

    override fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem) {
        binding.apply {
            setUI(bucketItemInfo, bucketListener)
            setDdayColor()
            executePendingBindings()
        }
    }

    override fun setUI(bucketItemInfo: BucketItem, bucketListener: View.OnClickListener) {
        super.setUI(bucketItemInfo, bucketListener)

        binding.bucketTitleText = bucketItemInfo.title

        binding.bucketClickListener = bucketListener

        binding.successButtonLayout.bucketSuccessListener = createOnClickBucketSuccessListener(bucketItemInfo)
        binding.bucketSuccessClickListener = createOnClickBucketSuccessLayoutListener(bucketItemInfo)

        if (Preference.getShowDdayFilter(binding.root.context)) {
            bucketItemInfo.dDay?.run {
                binding.isOverDday = this < 0
                binding.ddayText = if (this < 0) "D${this.toString().replace("-", "+")}" else "D-${this}"
            }
            binding.ddayTextView.visibility = if (bucketItemInfo.dDay != null) View.VISIBLE else View.INVISIBLE
        } else {
            binding.ddayTextView.visibility = View.GONE
        }

    }


}