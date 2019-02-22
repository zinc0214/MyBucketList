package womenproject.com.mybury.viewholder

import android.content.Context
import android.util.Log
import android.view.View
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.BucketItemBaseBinding

/**
 * Created by HanAYeon on 2019. 1. 9..
 */

open class BaseNormalBucketItemViewHolder(private val binding: BucketItemBaseBinding) : BaseBucketItemViewHolder(binding) {


    init {
        bucketItemLayout = binding.bucketItemLayout
        successImageView = binding.successButtonLayout.successImg
        bucketItemImage = binding.bucketItemImage
        bucketTitle = binding.bucketTitle
        circularProgressBar = binding.successButtonLayout.circularProgressBar
    }

    override fun bind(bucketListener: View.OnClickListener, bucketItemInfo : BucketItem, context : Context) {
        binding.apply {
            ddayVisible = bucketItemInfo.d_day > 0
            userCount = bucketItemInfo.user_count
            goalCount = bucketItemInfo.goal_count
            compelete = bucketItemInfo.complete
            isLastItem = bucketItemInfo.isLast
            bucketClickListener = bucketListener
            successButtonLayout.bucketSuccessListener = createOnClickBucketSuccessListener()
            bucketTitleText = bucketItemInfo.title

            Log.e("ayhan", "$isLastItem")
            lastImgVisible = if(isLastItem) {
                View.VISIBLE
            } else {
                View.GONE
            }


            executePendingBindings()
        }
    }

    private fun createOnClickBucketSuccessListener(): View.OnClickListener {
        return View.OnClickListener {
            onBucketSuccessFinalButtonClickListener()
        }
    }

}