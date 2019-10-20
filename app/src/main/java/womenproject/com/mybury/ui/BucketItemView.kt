package womenproject.com.mybury.ui

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
import womenproject.com.mybury.ui.loadingbutton.animatedDrawables.ProgressType
import womenproject.com.mybury.ui.loadingbutton.customView.CircularProgressButton
import womenproject.com.mybury.ui.loadingbutton.customView.ProgressButton

/**
 * Created by HanAYeon on 2019. 1. 10..
 */

class BucketItemView(private val binding: ViewDataBinding) {

    lateinit var bucketItemLayout: LinearLayout
    lateinit var successImageView: ImageView
    lateinit var bucketItemImage: ImageView
    lateinit var bucketTitle: TextView
    lateinit var circularProgressBar: CircularProgressButton
    lateinit var userCountText : TextView

    var userCount = 0
    var goalCount = 0
    var bucketType = 0
    val animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)


    private fun onBucketSuccessFinalButtonClickListener() {
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
                            animFadeOut.duration = 750
                            bucketItemImage.startAnimation(animFadeOut)
                        }, 800)
                        postDelayed({
                            bucketItemImage.visibility = View.GONE
                            bucketItemLayout.isClickable = true
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


    open fun setUI(bucketItemInfo: BucketItem, bucketListener: View.OnClickListener) {
    }

    open fun createOnClickBucketSuccessListener(): View.OnClickListener {
        return View.OnClickListener {
            onBucketSuccessFinalButtonClickListener()
        }
    }

    open fun createOnClickBucketSuccessLayoutListener(): View.OnClickListener {
        return View.OnClickListener {
            createOnClickBucketSuccessListener()
        }
    }

    open fun setFinalSuccessWithCountBucket() {}
    open fun addBucketSuccessCount() {}
    open fun setDdayColor() {}
}