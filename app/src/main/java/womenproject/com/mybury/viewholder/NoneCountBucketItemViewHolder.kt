package womenproject.com.mybury.viewholder

import android.content.Context
import android.view.View
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.BucketItemBaseBinding

/**
 * Created by HanAYeon on 2019. 1. 9..
 */

class NoneCountBucketItemViewHolder(private val isDdayView : Boolean, private val binding: BucketItemBaseBinding) : BaseBucketItemViewHolder(isDdayView, binding) {


    init {
        bucketItemLayout = binding.bucketItemLayout
        successImageView = binding.successButtonLayout.successImg
        bucketItemImage = binding.bucketItemImage
        bucketTitle = binding.bucketTitle
        circularProgressBar = binding.successButtonLayout.circularProgressBar
    }

    override fun bind(bucketListener: View.OnClickListener, bucketItemInfo : BucketItem, context : Context) {
        binding.apply {
            ddayVisible = bucketItemInfo.ddayVisible
            bucketClickListener = bucketListener
            successButtonLayout.bucketSuccessListener = createOnClickBucketSuccessListener()
            bucketTitleText = bucketItemInfo.title

            setDdayColor()

            executePendingBindings()
        }
    }

    private fun createOnClickBucketSuccessListener(): View.OnClickListener {
        return View.OnClickListener {
            onBucketSuccessFinalButtonClickListener()
        }
    }



    override fun setDdayColor() {
        if(isDdayView) {
            binding.successButtonLayout.circularProgressBar.spinningBarColor = context.getColor(R.color.notiColor)
            binding.bucketSucceedImage.background = context.getDrawable(R.drawable.dday_bucket_item_succeed_background)
            bucketItemImage.background = context.getDrawable(R.drawable.bucket_item_base_background)
        } else if(ddayVisible) {
            bucketItemImage.background = context.getDrawable(R.drawable.bucket_item_dday_background)
        }
    }


}