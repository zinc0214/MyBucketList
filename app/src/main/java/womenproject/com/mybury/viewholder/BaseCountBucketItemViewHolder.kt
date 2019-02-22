package womenproject.com.mybury.viewholder

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.BucketItemCountBinding

/**
 * Created by HanAYeon on 2019. 1. 24..
 */

open class BaseCountBucketItemViewHolder(private val binding: BucketItemCountBinding) : BaseBucketItemViewHolder(binding) {

    init {
        bucketType = 1
        initBinding = binding

        bucketItemLayout = binding.bucketItemLayout
        successImageView = binding.successButtonLayout.successImg
        bucketItemImage = binding.bucketItemImage
        bucketTitle = binding.bucketTitle
        circularProgressBar = binding.successButtonLayout.circularProgressBar
    }

    override fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem, context: Context) {
        binding.apply {
            ddayVisible = bucketItemInfo.d_day > 0
            userCount = bucketItemInfo.user_count
            goalCount = bucketItemInfo.goal_count
            compelete = bucketItemInfo.complete
            isLastItem = bucketItemInfo.isLast
            bucketTitleText = bucketItemInfo.title
            originSuccessCount = bucketItemInfo.user_count.toString()
            horizontalProgressBar.progress = bucketItemInfo.goal_count

            bucketClickListener = bucketListener
            successButtonLayout.bucketSuccessListener = createOnClickBucketSuccessListener()

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

    private fun setProgressMax(pb: ProgressBar, max: Int) {
        pb.max = max * 100
    }

    override fun setFinalSuccessWithCountBucket() {
        binding.progressBarLayout.visibility = View.GONE
    }

    override fun addBucketSuccessCount() {
        binding.originSuccessCount = (userCount + 1).toString()
        binding.horizontalProgressBar.progress = userCount + 1
        userCount += 1

        setProgressMax(binding.horizontalProgressBar, goalCount)
        setProgressAnimate(binding.horizontalProgressBar, userCount)
    }

    private fun setProgressAnimate(pb: ProgressBar, progressTo: Int) {
        val animation = ObjectAnimator.ofInt(pb, "progress", (progressTo - 1) * 100, progressTo * 100)
        animation.duration = 1000
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }

}