package womenproject.com.mybury.viewholder

import android.animation.ObjectAnimator
import android.content.Context
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
        userCountText = binding.userCount
    }

    override fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem, context: Context) {
        binding.apply {

            setBucketData(bucketItemInfo)
            setUI(bucketItemInfo, bucketListener)
            setDdayColor()

            executePendingBindings()

        }
    }

    override fun setUI(bucketItemInfo: BucketItem, bucketListener: View.OnClickListener) {
        super.setUI(bucketItemInfo, bucketListener)

        binding.bucketTitleText = bucketItemInfo.title
        binding.currentUserCount = bucketItemInfo.userCount.toString()
        binding.horizontalProgressBar.progress = bucketItemInfo.goalCount

        binding.bucketClickListener = bucketListener
        binding.successButtonLayout.bucketSuccessListener = createOnClickBucketSuccessListener()

        binding.lastEndImg.lastImgVisible = if(isLastItem) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }



    private fun setProgressMax(pb: ProgressBar, max: Int) {
        pb.max = max * 100
    }

    override fun setFinalSuccessWithCountBucket() {
        binding.progressBarLayout.visibility = View.GONE
    }

    override fun addBucketSuccessCount() {
        binding.currentUserCount = (userCount + 1).toString()
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