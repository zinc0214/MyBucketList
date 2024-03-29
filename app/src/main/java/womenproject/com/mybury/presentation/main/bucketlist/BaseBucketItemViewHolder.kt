package womenproject.com.mybury.presentation.main.bucketlist

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Handler
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.databinding.ItemBucketDoingSimpleBinding
import womenproject.com.mybury.ui.loadingbutton.animatedDrawables.ProgressType
import womenproject.com.mybury.ui.loadingbutton.customView.ProgressButton
import womenproject.com.mybury.util.Converter.Companion.dpToPx

/**
 * Created by HanAYeon on 2019. 1. 10..
 */

open class BaseBucketItemViewHolder(
    private val binding: ItemBucketDoingSimpleBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var isForDday = false

    fun bind(
        bucketItemInfo: BucketItem,
        isForDday: Boolean = false,
        isShowDday: Boolean = false,
        isLastItem: Boolean = false,
        bucketItemHandler: BucketItemHandler
    ) {
        this.isForDday = isForDday

        if (isForDday) {
            setDdayColor()
        }
        setUI(bucketItemInfo, bucketItemHandler, isShowDday, isLastItem)
        binding.executePendingBindings()
    }

    private fun startBucketCompleteAnimation(
        info: BucketItem,
        animFinished: () -> Unit
    ) {
        val animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)

        binding.bucketItemLayout.isEnabled = false
        binding.apply {
            binding.completeCircleView.circularProgressBar.run {
                progressType = ProgressType.INDETERMINATE
                startAnimation()
                progressAnimator(this).start()
                Handler().run {
                    bucketItemLayout.isClickable = false
                    postDelayed({
                        setFinalSuccessUIButton()
                        addBucketCompleteCount(info)
                    }, 500)
                    postDelayed({
                        revertAnimation()
                        if (info.goalCount == 1 || info.userCount >= info.goalCount) {
                            setFinalSuccessUIBackground()
                        } else {
                            setDoneSuccessUIButton()
                        }
                    }, 750)
                    postDelayed({
                        if (info.goalCount == 1 || info.userCount >= info.goalCount) {
                            animFadeOut.duration = 750
                            bucketItemImage.startAnimation(animFadeOut)
                        }
                    }, 900)
                    postDelayed({
                        if (info.goalCount == 1 || info.userCount >= info.goalCount) {
                            bucketItemImage.visibility = View.GONE
                            binding.ddayTextView.visibility = View.INVISIBLE
                        }
                        info.userCount += 1
                        bucketItemLayout.isClickable = true
                    }, 1000)
                }
            }
            binding.bucketItemLayout.isEnabled = true
            animFinished.invoke()
        }
    }

    private fun progressAnimator(progressButton: ProgressButton): ValueAnimator =
        ValueAnimator.ofFloat(0F, 100F).apply {
            duration = 700
            startDelay = 0
            addUpdateListener { animation ->
                progressButton.setProgress(animation.animatedValue as Float)
            }

            binding.bucketItemImage.setBackgroundResource(
                if (isForDday) {
                    R.drawable.shape_ffffff_r4_strk_13_ffca5a
                } else {
                    R.drawable.shape_ffffff_r4_strk_13_a6c6ff
                }
            )
        }

    private fun setFinalSuccessUIButton() {
        if (isForDday) {
            binding.completeCircleView.successImg.backgroundTintList =
                context.getColorStateList(R.color._ffca5a)
        } else {
            binding.completeCircleView.successImg.backgroundTintList =
                context.getColorStateList(R.color._efefef)
        }
    }

    private fun setFinalSuccessUIBackground() {
        binding.completeCircleView.successImg.setBackgroundResource(R.drawable.check_complete)
        binding.completeCircleView.successImg.backgroundTintList =
            context.getColorStateList(R.color._d8d7d7)
        binding.bucketTitle.setTextColor(context.resources.getColor(R.color._434343))
        binding.bucketTitle.alpha = 0.6f
        binding.completeCircleView.circularProgressBar.visibility = View.GONE
        binding.bucketItemImage.setBackgroundResource(R.drawable.shape_ffffff_r4_strk_e8e8e8)
        binding.progressLayout.visibility = View.GONE
    }

    private fun setDoneSuccessUIButton() {
        binding.completeCircleView.successImg.backgroundTintList =
            context.getColorStateList(R.color._e8e8e8)
        binding.bucketItemImage.setBackgroundResource(R.drawable.shape_ffffff_r4_strk_06_e8e8e8)
    }

    private fun setUI(
        bucketItemInfo: BucketItem,
        bucketItemHandler: BucketItemHandler,
        isShowDday: Boolean,
        isLastItem: Boolean
    ) {
        binding.bucketInfo = bucketItemInfo
        binding.bucketClickListener = View.OnClickListener {
            bucketItemHandler.bucketSelect(bucketItemInfo)
        }
        binding.completeCircleView.bucketSuccessListener = View.OnClickListener {
            setOnClickBucketCompleteLayoutListener(bucketItemInfo, bucketItemHandler)
        }
        if (Preference.getShowDdayFilter(binding.root.context) || isShowDday) {
            binding.ddayTextView.visibility =
                if (bucketItemInfo.dDay != null) View.VISIBLE else View.INVISIBLE
        } else {
            binding.ddayTextView.visibility = View.GONE
        }

        binding.userCount.text = setCountText(bucketItemInfo.userCount, bucketItemInfo.goalCount)

        if (isLastItem) {
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(dpToPx(8))
            binding.bucketItemLayout.layoutParams = lp
        }
    }

    private fun setOnClickBucketCompleteLayoutListener(
        bucketItemInfo: BucketItem,
        bucketItemHandler: BucketItemHandler
    ) {
        startBucketCompleteAnimation(bucketItemInfo) {
            bucketItemHandler.bucketComplete(bucketItemInfo)
        }
    }

    private fun addBucketCompleteCount(info: BucketItem) {
        if (info.goalCount <= 1) return

        binding.userCount.text = setCountText(info.userCount + 1, info.goalCount)
        binding.horizontalProgressBar.progress = info.userCount + 1
        setProgressMax(binding.horizontalProgressBar, info.goalCount)
        setProgressAnimate(binding.horizontalProgressBar, info.userCount + 1)
    }

    private fun setDdayColor() {
        binding.bucketItemImage.setBackgroundResource(R.drawable.bucket_dday_click_background)
        binding.bucketSucceedImage.setBackgroundResource(R.drawable.shape_efefef_r4_strk_e8e8e8)
        binding.completeCircleView.circularProgressBar.spinningBarColor =
            context.getColor(R.color._ffca5a)
        binding.horizontalProgressBar.progressDrawable =
            context.getDrawable(R.drawable.dday_horizontal_progressbar)
        binding.ddayTextView.visibility = View.GONE
    }

    private fun setCountText(userCount: Int, goalCount: Int): Spanned {
        val countText: String = if (isForDday) {
            context.resources.getString(R.string.dday_bucket_count)
        } else {
            context.resources.getString(R.string.bucket_count)
        }

        return Html.fromHtml(
            String.format(
                countText,
                "$userCount", "/${goalCount}"
            )
        )
    }

    private fun setProgressMax(pb: ProgressBar, max: Int) {
        pb.max = max * 100
    }

    private fun setProgressAnimate(pb: ProgressBar, progressTo: Int) {
        val animation = ObjectAnimator.ofInt(
            pb, "progress",
            (progressTo - 1) * 100, progressTo * 100
        )
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }
}

interface BucketItemHandler {
    fun bucketSelect(itemInfo: BucketItem)
    fun bucketComplete(itemInfo: BucketItem)
}