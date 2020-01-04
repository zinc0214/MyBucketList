package womenproject.com.mybury.presentation.mypage.dday

import android.animation.ValueAnimator
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.BucketItemBaseBinding
import womenproject.com.mybury.ui.loadingbutton.customView.ProgressButton
import womenproject.com.mybury.presentation.main.bucketlist.BaseNormalBucketItemViewHolder

/**
 * Created by HanAYeon on 2019. 1. 24..
 */


class DdayNormalBucketItemViewHolder(private val binding: BucketItemBaseBinding) : BaseNormalBucketItemViewHolder(binding) {

    override fun progressAnimator(progressButton: ProgressButton) = ValueAnimator.ofFloat(0F, 100F).apply {
        duration = 500
        startDelay = 0
        addUpdateListener { animation ->
            progressButton.setProgress(animation.animatedValue as Float)
        }
        bucketItemImage.setBackgroundResource(R.drawable.bucket_item_dday_background)

    }

    override fun setFinalSuccessUIButton() {
        successImageView.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color._ffca5a)
    }

    override fun setFinalSuccessUIBackground() {
        super.setFinalSuccessUIBackground()
        bucketItemImage.setBackgroundResource(R.drawable.dday_bucket_item_success_background)
    }

    override fun setDoneSuccessUIButton() {
        successImageView.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color._e8e8e8)
        bucketItemImage.background = MyBuryApplication.context.getDrawable(R.drawable.bucket_item_base_background)

    }

    override fun setDdayColor() {
        binding.bucketItemImage.background = MyBuryApplication.context.getDrawable(R.drawable.bucket_dday_click_background)
        binding.successButtonLayout.circularProgressBar.spinningBarColor = MyBuryApplication.context.getColor(R.color._ffca5a)
        binding.bucketSucceedImage.background = MyBuryApplication.context.getDrawable(R.drawable.dday_bucket_item_succeed_background)

    }
}