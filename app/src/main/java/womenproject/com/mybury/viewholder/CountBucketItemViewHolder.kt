package womenproject.com.mybury.viewholder

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.BucketItemCountBinding
import womenproject.com.mybury.util.loadingbutton.animatedDrawables.ProgressType
import womenproject.com.mybury.util.loadingbutton.customView.ProgressButton

/**
 * Created by HanAYeon on 2019. 1. 9..
 */

class CountBucketItemViewHolder(private val binding: BucketItemCountBinding) : BaseBucketItemViewHolder(binding) {

    private var previousValue = 0

    override fun bind(bucketListener: View.OnClickListener, bucektItemInfo: BucketItem, context: Context) {
        binding.apply {
            if (bucektItemInfo.dday == 1) {
                bucketItemLayout.background = context.getDrawable(R.drawable.bucket_dday_item_background)
            }
            if (bucektItemInfo.bucketType == 1) {
                progressBarVisible = View.GONE
            } else {
                progressBarVisible = View.VISIBLE
            }
            successImgVisible = View.GONE
            previousValue = bucektItemInfo.count
            bucket = bucektItemInfo.title
            currentValue = bucektItemInfo.count.toString()
            horizontalProgressBar.progress = bucektItemInfo.count
            bucketClickListener = bucketListener
            circularProgressBar.run {
                setOnClickListener {
                    progressType = ProgressType.INDETERMINATE
                    startAnimation()
                    progressAnimator(this).start()
                    Handler().run {
                        postDelayed({
                            doneLoadingAnimation(defaultColor(context), defaultDoneImage(context.resources))
                            bucketItemLayout.setBackgroundResource(R.drawable.bucket_item_success_background)
                            if(bucektItemInfo.bucketType == 0) {
                                progressBarVisible = View.GONE
                                addCurrentValue()
                            }
                            successImgVisible = View.VISIBLE
                        }, 2500)
                        postDelayed({
                            revertAnimation()
                            bucketItemLayout.setBackgroundResource(R.drawable.bucket_item_background)
                            if(bucektItemInfo.bucketType == 0) {
                                progressBarVisible = View.VISIBLE
                            }
                            successImgVisible = View.GONE
                        }, 3500)
                    }
                }
            }
            executePendingBindings()
        }
    }

    private fun addCurrentValue() {
        binding.currentValue = (previousValue + 1).toString()
        binding.horizontalProgressBar.progress = previousValue + 1
        previousValue = previousValue + 1
    }


    private fun progressAnimator(progressButton: ProgressButton) = ValueAnimator.ofFloat(0F, 100F).apply {
        duration = 1500
        startDelay = 0
        addUpdateListener { animation ->
            progressButton.setProgress(animation.animatedValue as Float)
        }
    }

    private fun defaultColor(context: Context) = ContextCompat.getColor(context, android.R.color.transparent)

    private fun defaultDoneImage(resources: Resources) =
            BitmapFactory.decodeResource(resources, R.drawable.check_complete)


}