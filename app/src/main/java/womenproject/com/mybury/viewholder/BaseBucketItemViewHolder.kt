package womenproject.com.mybury.viewholder

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.util.loadingbutton.animatedDrawables.ProgressType
import womenproject.com.mybury.util.loadingbutton.customView.CircularProgressButton
import womenproject.com.mybury.util.loadingbutton.customView.ProgressButton

/**
 * Created by HanAYeon on 2019. 1. 10..
 */

abstract class BaseBucketItemViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem, context: Context)
    lateinit var initBinding : ViewDataBinding

    var initProgressBarVisible = View.VISIBLE
    var previousValue = 0
    var bucketType = 0
    var bucketDDay = 0


    fun onBucketSuccessFinalButtonClickListener(circularProgressBar: CircularProgressButton, bucketLayout: LinearLayout) {
        binding.apply {
            circularProgressBar.run {
                setOnClickListener {
                    progressType = ProgressType.INDETERMINATE
                    startAnimation()
                    progressAnimator(this).start()
                    Handler().run {
                        postDelayed({
                            setFinalSuccessUIButton()
                            addCurrentValue()
                        }, 1000)
                        postDelayed({
                            revertAnimation()
                            if (bucketType == 0 || previousValue >= 10) {
                                setFinalSuccessUIBackground()
                            } else {
                                setDoneSuccessUIButton()
                            }
                        }, 1500)
                        postDelayed({
                            if (bucketType == 0 || previousValue >= 10) {
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
        duration = 1000
        startDelay = 0
        addUpdateListener { animation ->
            progressButton.setProgress(animation.animatedValue as Float)
        }
    }

    abstract fun setFinalSuccessUIButton()
    abstract fun setFinalSuccessUIBackground()
    abstract fun setFinalSucceedUIBackground()
    abstract fun setDoneSuccessUIButton()
    abstract fun addCurrentValue()

}