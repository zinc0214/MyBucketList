package womenproject.com.mybury.presentation.util

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.presentation.base.BaseBucketItemViewHolder
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
        bucketTitle = binding.succeedBucketTitle
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
        binding.succeedBucketTitle.text = bucketItemInfo.title
        binding.currentUserCount = bucketItemInfo.userCount.toString()
        binding.horizontalProgressBar.progress = bucketItemInfo.userCount

        binding.bucketClickListener = bucketListener
        binding.successButtonLayout.bucketSuccessListener = createOnClickBucketSuccessListener()
        binding.bucketSuccessClickListener = createOnClickBucketSuccessLayoutListener()

        binding.lastEndImg.lastImgVisible = if (isLastItem) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun createOnClickBucketSuccessLayoutListener(): View.OnClickListener {
        return View.OnClickListener {
            binding.successButtonLayout.circularProgressBar.callOnClick()
        }
    }


    private fun setProgressMax(pb: ProgressBar, max: Int) {
        pb.max = max * 100
    }

    override fun setFinalSuccessWithCountBucket() {
        binding.prgressLayout.visibility = View.GONE
        binding.contentLayout.visibility = View.GONE
        binding.countSucceedLayout.visibility = View.VISIBLE

        val bucketItemLayout = binding.bucketItemLayout.getLayoutParams() as LinearLayout.LayoutParams

        if (binding.bucketTitle.lineCount >= 2) {
            bucketItemLayout.height += context.resources.getDimensionPixelSize(R.dimen.titleMargin)
        }


        val bucketTitle = binding.bucketTitle.getLayoutParams() as RelativeLayout.LayoutParams
        bucketTitle.topMargin = 0
        bucketTitle.addRule(RelativeLayout.CENTER_VERTICAL)
        binding.bucketTitle.layoutParams = bucketTitle
        //binding.contentLayout.layoutParams = contentLayout

        val contentLayout = binding.contentLayout.getLayoutParams() as RelativeLayout.LayoutParams
        contentLayout.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
        binding.contentLayout.layoutParams = contentLayout
        binding.contentLayout.gravity = RelativeLayout.CENTER_VERTICAL

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