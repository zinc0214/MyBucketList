package womenproject.com.mybury.presentation.mypage.dday

import android.animation.ValueAnimator
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.BucketItemCountBinding
import womenproject.com.mybury.ui.loadingbutton.customView.ProgressButton
import womenproject.com.mybury.presentation.base.BaseCountBucketItemViewHolder


/**
 * Created by HanAYeon on 2019. 1. 9..
 */

class DdayCountBucketItemViewHolder(private val binding: BucketItemCountBinding) : BaseCountBucketItemViewHolder(binding) {

    override fun progressAnimator(progressButton: ProgressButton) = ValueAnimator.ofFloat(0F, 100F).apply {
        duration = 500
        startDelay = 0
        addUpdateListener { animation ->
            progressButton.setProgress(animation.animatedValue as Float)
        }
        bucketItemImage.setBackgroundResource(R.drawable.bucket_item_dday_background)

    }

    override fun setFinalSuccessUIButton() {
        successImageView.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color.ddayBucketSuccessBtnBackground)
    }

    override fun setFinalSuccessUIBackground() {
        super.setFinalSuccessUIBackground()
        bucketItemImage.setBackgroundResource(R.drawable.dday_bucket_item_success_background)
    }

    override fun setDoneSuccessUIButton() {
        successImageView.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color.bucketBaseBtnBackground)
        bucketItemImage.background = MyBuryApplication.context.getDrawable(R.drawable.bucket_item_base_background)

    }

    override fun setDdayColor() {
        binding.horizontalProgressBar.progressDrawable = context.getDrawable(R.drawable.dday_horizontal_progressbar)
        binding.successButtonLayout.circularProgressBar.spinningBarColor = context.getColor(R.color.notiColor)
        binding.bucketSucceedImage.background = context.getDrawable(R.drawable.dday_bucket_item_succeed_background)
        binding.userCount.setTextColor(context.getColor(R.color.notiColor))
    }
}