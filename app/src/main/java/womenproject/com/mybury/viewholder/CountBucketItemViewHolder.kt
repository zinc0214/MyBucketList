package womenproject.com.mybury.viewholder

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.BucketItemCountBinding


/**
 * Created by HanAYeon on 2019. 1. 9..
 */

class CountBucketItemViewHolder(private val binding: BucketItemCountBinding) : BaseBucketItemViewHolder(binding) {

    init {
        bucketType = 1
        initBinding = binding

        bucketItemLayout = binding.bucketItemLayout
        successImageView = binding.successImg
        bucketItemImage = binding.bucketItemImage
        bucketTitle = binding.bucketTitle
        circularProgressBar = binding.circularProgressBar
    }

    override fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem, context: Context) {
        binding.apply {
            bucketDDay = bucketItemInfo.dday
            if (bucketItemInfo.dday == 1) {
                bucketItemImage.background = context.getDrawable(R.drawable.bucket_item_dday_background)
            }
            currentSuccessCount = bucketItemInfo.count
            bucketTitleText = bucketItemInfo.title
            originSuccessCount = bucketItemInfo.count.toString()
            horizontalProgressBar.progress = bucketItemInfo.count

            bucketClickListener = bucketListener
            bucketSuccessListener = createOnClickBucketSuccessListener()
            

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
        binding.originSuccessCount = (currentSuccessCount + 1).toString()
        binding.horizontalProgressBar.progress = currentSuccessCount + 1
        currentSuccessCount += 1

        setProgressMax(binding.horizontalProgressBar, 10)
        setProgressAnimate(binding.horizontalProgressBar, currentSuccessCount)
    }

    private fun setProgressAnimate(pb: ProgressBar, progressTo: Int) {
        val animation = ObjectAnimator.ofInt(pb, "progress", (progressTo - 1) * 100, progressTo * 100)
        animation.duration = 1000
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }
}