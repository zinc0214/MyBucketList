package womenproject.com.mybury.presentation.mypage.dday

import android.animation.ValueAnimator
import android.view.View
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.ItemBucketDoingCountBinding
import womenproject.com.mybury.presentation.main.bucketlist.CountBucketItemViewHolder
import womenproject.com.mybury.ui.loadingbutton.customView.ProgressButton

/**
 * Created by HanAYeon on 2019. 1. 24..
 */


class DdayCountBucketItemViewHolder(
    private val binding: ItemBucketDoingCountBinding,
    private val showSnackBar: ((BucketItem) -> Unit)
) : CountBucketItemViewHolder(true, binding, showSnackBar) {

    init {
        setDdayColor()
    }

    override fun progressAnimator(progressButton: ProgressButton) =
        ValueAnimator.ofFloat(0F, 100F).apply {
            duration = 500
            startDelay = 0
            addUpdateListener { animation ->
                progressButton.setProgress(animation.animatedValue as Float)
            }
            bucketItemImage.setBackgroundResource(R.drawable.shape_ffffff_r4_strk_13_ffca5a)

        }

    override fun setFinalSuccessUIButton() {
        successImageView.backgroundTintList =
           binding.root.context.getColorStateList(R.color._ffca5a)
    }

    override fun setFinalSuccessUIBackground() {
        super.setFinalSuccessUIBackground()
        bucketItemImage.setBackgroundResource(R.drawable.shape_ffffff_r4_strk_e8e8e8)
    }

    override fun setDoneSuccessUIButton() {
        successImageView.backgroundTintList =
           binding.root.context.getColorStateList(R.color._e8e8e8)
        bucketItemImage.background =
           binding.root.context.getDrawable(R.drawable.shape_ffffff_r4_strk_06_e8e8e8)

    }

    override fun setDdayColor() {
        binding.bucketItemLayout.setBackgroundResource(R.drawable.bucket_dday_click_background)
        binding.horizontalProgressBar.progressDrawable =
           binding.root.context.getDrawable(R.drawable.dday_horizontal_progressbar)
        binding.successButtonView.circularProgressBar.spinningBarColor =
           binding.root.context.getColor(R.color._ffca5a)
        binding.bucketSucceedImage.setBackgroundResource(R.drawable.shape_efefef_r4_strk_e8e8e8)
        binding.ddayTextView.visibility = View.GONE
    }
}