package womenproject.com.mybury.base

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
    lateinit var userCountText : TextView

    var userCount = 0
    var goalCount = 0
    var bucketType = 0
    var compelete = false
    var ddayVisible = false
    var isLastItem = false
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
                            if (bucketType == 0 || userCount >= goalCount) {
                                setFinalSuccessUIBackground()
                                setFinalSuccessWithCountBucket()
                            } else {
                                setDoneSuccessUIButton()
                            }
                        }, 750)
                        postDelayed({
                            if (bucketType == 0 || userCount >= goalCount) {
                                animFadeOut.duration = 700
                                bucketItemImage.startAnimation(animFadeOut)
                            }
                        }, 800)
                        postDelayed({
                            if (bucketType == 0 || userCount >= goalCount) {
                                bucketItemImage.visibility = View.GONE
                                bucketItemLayout.isClickable = true
                            }

                        }, 900)
                    }
                }
            }
            executePendingBindings()
        }
    }

    open fun progressAnimator(progressButton: ProgressButton) = ValueAnimator.ofFloat(0F, 100F).apply {
        duration = 700
        startDelay = 0
        addUpdateListener { animation ->
            progressButton.setProgress(animation.animatedValue as Float)
        }
        bucketItemImage.setBackgroundResource(R.drawable.bucket_item_successing_background)
    }

    open fun setFinalSuccessUIButton() {
        successImageView.backgroundTintList = context.getColorStateList(R.color.bucketSuccessBtnBackground)
    }


    open fun setFinalSuccessUIBackground() {
        successImageView.setBackgroundResource(R.drawable.check_complete)
        successImageView.backgroundTintList = context.getColorStateList(R.color.white)
        bucketTitle.setTextColor(context.resources.getColor(R.color.white))
        circularProgressBar.visibility = View.GONE
        bucketItemLayout.layoutParams.height = context.resources.getDimension(R.dimen.minimumBucketItemHeight).toInt()

        bucketItemImage.setBackgroundResource(R.drawable.bucket_item_success_background)
    }

    open fun setDoneSuccessUIButton() {
        successImageView.backgroundTintList = context.getColorStateList(R.color.bucketBaseBtnBackground)
        bucketItemImage.background = context.getDrawable(R.drawable.bucket_item_base_background)

    }

    open fun setBucketData(bucketItem : BucketItem) {
        ddayVisible = bucketItem.dDay > 0
        userCount = bucketItem.userCount
        goalCount = bucketItem.goalCount
        compelete = bucketItem.complete
        isLastItem = bucketItem.isLast
    }

    open fun setUI(bucketItemInfo: BucketItem, bucketListener: View.OnClickListener) {
    }

    open fun createOnClickBucketSuccessListener(): View.OnClickListener {
        return View.OnClickListener {
            onBucketSuccessFinalButtonClickListener()
        }
    }

    open fun setFinalSuccessWithCountBucket() {}
    open fun addBucketSuccessCount() {}
    open fun setDdayColor() {}
}