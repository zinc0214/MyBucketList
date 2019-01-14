package womenproject.com.mybury.viewholder

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.util.loadingbutton.animatedDrawables.ProgressType
import womenproject.com.mybury.util.loadingbutton.customView.CircularProgressButton
import womenproject.com.mybury.util.loadingbutton.customView.ProgressButton

/**
 * Created by HanAYeon on 2019. 1. 10..
 */

abstract class BaseBucketItemViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem, context: Context)

    var initProgressBarVisible = View.VISIBLE
    var previousValue = 0
    var bucketType = 0


    fun onBucketSuccessFinalButtonClickListener(circularProgressBar: CircularProgressButton, bucketLayout : LinearLayout) {
        binding.apply {
            circularProgressBar.run {
                setOnClickListener {
                    progressType = ProgressType.INDETERMINATE
                    bucketLayout.isClickable = false
                    startAnimation()
                    progressAnimator(this).start()
                    Handler().run {
                        postDelayed({
                            setFinalSuccessUIButton()
                            addCurrentValue()

                        }, 1000)
                        postDelayed({
                            if(bucketType == 0 || previousValue >= 9) {
                                setFinalSuccessUIBackground()
                            } else {
                                setInitSuccuessUIButton()
                            }
                        }, 1500)
                        postDelayed({
                            revertAnimation()
                            if(bucketType == 0 || previousValue >= 9) {
                                setFinalSucceedUIBackground()
                            }
                        }, 2000)
                    }
                }
            }
            executePendingBindings()
        }
    }

    private fun progressAnimator(progressButton: ProgressButton) = ValueAnimator.ofFloat(0F, 100F).apply {
        duration = 1500
        startDelay = 0
        addUpdateListener { animation ->
            progressButton.setProgress(animation.animatedValue as Float)
        }
    }

    abstract fun setFinalSuccessUIButton()
    abstract fun setFinalSuccessUIBackground()
    abstract fun setFinalSucceedUIBackground()
    abstract fun setInitSuccuessUIButton()
    abstract fun addCurrentValue()

}