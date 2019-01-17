package womenproject.com.mybury.viewholder

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.MyBuryApplication.Companion.context
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
    lateinit var initBinding: ViewDataBinding

    lateinit var bucketItemLayout: LinearLayout
    lateinit var successImageView: ImageView
    lateinit var bucketItemImage: ImageView
    lateinit var bucketTitle: TextView
    lateinit var circularProgressBar: CircularProgressButton

    var initProgressBarVisible = View.VISIBLE
    var currentSuccessCount = 0
    var bucketType = 0
    var bucketDDay = 0
    val animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)


    fun onBucketSuccessFinalButtonClickListener() {
        binding.apply {
            circularProgressBar.run {
                setOnClickListener {
                    progressType = ProgressType.INDETERMINATE
                    startAnimation()
                    progressAnimator(this).start()
                    Handler().run {
                        bucketItemLayout.isClickable = false
                        postDelayed({
                            setFinalSuccessUIButton()
                            addBucketSuccessCount()
                        }, 500)
                        postDelayed({
                            revertAnimation()
                            if (bucketType == 0 || currentSuccessCount >= 10) {
                                setFinalSuccessUIBackground()
                                setFinalSuccessWithCountBucket()
                            } else {
                                setDoneSuccessUIButton()
                            }
                        }, 1000)
                        postDelayed({
                            if (bucketType == 0 || currentSuccessCount >= 10) {
                                animFadeOut.duration = 500
                                bucketItemImage.startAnimation(animFadeOut)
                            }
                        }, 1500)
                        postDelayed({
                            if (bucketType == 0 || currentSuccessCount >= 10) {
                                //set.end()

                                bucketItemImage.visibility = View.GONE
                                bucketItemLayout.isClickable = true
                            }

                        }, 2000)
                    }
                }
            }
            executePendingBindings()
        }
    }

    private fun progressAnimator(progressButton: ProgressButton) = ValueAnimator.ofFloat(0F, 100F).apply {
        duration = 500
        startDelay = 0
        addUpdateListener { animation ->
            progressButton.setProgress(animation.animatedValue as Float)
        }
    }

    private fun setFinalSuccessUIButton() {
        successImageView.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color.bucket_success_btn_background)
        bucketItemImage.setBackgroundResource(R.drawable.bucket_item_successing_background)
    }

    private fun setFinalSuccessUIBackground() {
        successImageView.setBackgroundResource(R.drawable.check_complete)
        successImageView.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color.white)
        bucketItemImage.setBackgroundResource(R.drawable.bucket_item_success_background)
        bucketTitle.setTextColor(MyBuryApplication.context.resources.getColor(R.color.white))
        circularProgressBar.visibility = View.GONE
        bucketItemLayout.layoutParams.height = context.resources.getDimension(R.dimen.minimumBucketItemHeight).toInt()
    }

    private fun setDoneSuccessUIButton() {
        successImageView.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color.bucket_base_btn_background)
        if (bucketDDay == 1) {
            bucketItemImage.background = MyBuryApplication.context.getDrawable(R.drawable.bucket_item_dday_background)
        } else {
            bucketItemImage.background = MyBuryApplication.context.getDrawable(R.drawable.bucket_item_base_background)
        }
    }

    open fun setFinalSuccessWithCountBucket() {}
    open fun addBucketSuccessCount() {}

}