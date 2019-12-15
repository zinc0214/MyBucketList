package womenproject.com.mybury.presentation.main.bucketlist

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
import womenproject.com.mybury.databinding.BucketItemCountBinding


/**
 * Created by HanAYeon on 2019. 1. 24..
 */

open class BaseCountBucketItemViewHolder(private val binding: BucketItemCountBinding) : BaseBucketItemViewHolder(binding) {

    private lateinit var info : BucketItem
    init {
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
            setUI(bucketItemInfo, bucketListener)
            setDdayColor()
            executePendingBindings()
            info = bucketItemInfo
        }
    }

    override fun setUI(bucketItemInfo: BucketItem, bucketListener: View.OnClickListener) {
        binding.bucketTitleText = bucketItemInfo.title
        binding.succeedBucketTitle.text = bucketItemInfo.title
        binding.currentUserCount = bucketItemInfo.userCount.toString()
        binding.horizontalProgressBar.progress = bucketItemInfo.userCount
        binding.horizontalProgressBar.max = bucketItemInfo.goalCount

        binding.bucketClickListener = bucketListener

        val tokenId = getAccessToken(context)
        binding.successButtonLayout.bucketSuccessListener = createOnClickBucketSuccessListener(tokenId, bucketItemInfo)
        binding.bucketSuccessClickListener = createOnClickBucketSuccessLayoutListener(tokenId, bucketItemInfo)
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

        val contentLayout = binding.contentLayout.getLayoutParams() as RelativeLayout.LayoutParams
        contentLayout.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
        binding.contentLayout.layoutParams = contentLayout
        binding.contentLayout.gravity = RelativeLayout.CENTER_VERTICAL

    }

    override fun addBucketSuccessCount() {
        binding.currentUserCount = (info.userCount + 1).toString()
        binding.horizontalProgressBar.progress = info.userCount + 1
        info.userCount += 1
        setProgressMax(binding.horizontalProgressBar, info.goalCount)
        setProgressAnimate(binding.horizontalProgressBar, info.userCount)
    }

    private fun setProgressAnimate(pb: ProgressBar, progressTo: Int) {
        val animation = ObjectAnimator.ofInt(pb, "progress", (progressTo - 1) * 100, progressTo * 100)
        animation.duration = 1000
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }

}