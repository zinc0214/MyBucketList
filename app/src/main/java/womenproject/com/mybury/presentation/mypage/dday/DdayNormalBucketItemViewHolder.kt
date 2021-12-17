package womenproject.com.mybury.presentation.mypage.dday

import android.animation.ValueAnimator
import android.view.View
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.ItemBucketDoingSimpleBinding
import womenproject.com.mybury.presentation.main.bucketlist.BaseNormalBucketItemViewHolder
import womenproject.com.mybury.ui.loadingbutton.customView.ProgressButton

/**
 * Created by HanAYeon on 2019. 1. 24..
 */


class DdayNormalBucketItemViewHolder(private val binding: ItemBucketDoingSimpleBinding,
                                     private val showSnackBar: ((BucketItem) -> Unit)) : BaseNormalBucketItemViewHolder(binding, showSnackBar) {

    override fun progressAnimator(progressButton: ProgressButton) = ValueAnimator.ofFloat(0F, 100F).apply {
        duration = 500
        startDelay = 0
        addUpdateListener { animation ->
            progressButton.setProgress(animation.animatedValue as Float)
        }
        bucketItemImage.setBackgroundResource(R.drawable.shape_ffffff_r4_strk_13_ffca5a)

    }

    override fun setFinalSuccessUIButton() {
        successImageView.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color._ffca5a)
    }

    override fun setFinalSuccessUIBackground() {
        super.setFinalSuccessUIBackground()
        bucketItemImage.setBackgroundResource(R.drawable.shape_ffffff_r4_strk_e8e8e8)
    }

    override fun setDoneSuccessUIButton() {
        successImageView.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color._e8e8e8)
        bucketItemImage.background = MyBuryApplication.context.getDrawable(R.drawable.shape_ffffff_r4_strk_06_e8e8e8)

    }

    override fun setDdayColor() {
        binding.bucketItemImage.background = MyBuryApplication.context.getDrawable(R.drawable.bucket_dday_click_background)
        binding.successButtonLayout.circularProgressBar.spinningBarColor = MyBuryApplication.context.getColor(R.color._ffca5a)
        binding.bucketSucceedImage.background = MyBuryApplication.context.getDrawable(R.drawable.shape_efefef_r4_strk_e8e8e8)

        binding.ddayTextView.visibility = View.GONE
    }
}