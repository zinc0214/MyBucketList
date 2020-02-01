package womenproject.com.mybury.presentation.main.bucketlist

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.detail.BucketDetailViewModel
import womenproject.com.mybury.ui.loadingbutton.animatedDrawables.ProgressType
import womenproject.com.mybury.ui.loadingbutton.customView.CircularProgressButton
import womenproject.com.mybury.ui.loadingbutton.customView.ProgressButton

/**
 * Created by HanAYeon on 2019. 1. 10..
 */

abstract class BaseBucketItemViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem)

    lateinit var initBinding: ViewDataBinding

    lateinit var bucketItemLayout: LinearLayout
    lateinit var successImageView: ImageView
    lateinit var bucketItemImage: ImageView
    lateinit var bucketTitle: TextView
    lateinit var circularProgressBar: CircularProgressButton
    lateinit var userCountText: TextView

    val animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)

    fun onBucketSuccessFinalButtonClickListener(info: BucketItem) {
        binding.apply {
            circularProgressBar.run {
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
                        if (info.goalCount == 1 || info.userCount >= info.goalCount) {
                            setFinalSuccessUIBackground()
                            setFinalSuccessWithCountBucket()
                        } else {
                            setDoneSuccessUIButton()
                        }
                    }, 750)
                    postDelayed({
                        if (info.goalCount == 1 || info.userCount >= info.goalCount) {
                            animFadeOut.duration = 750
                            bucketItemImage.startAnimation(animFadeOut)
                        }
                    }, 800)
                    postDelayed({
                        if (info.goalCount == 1 || info.userCount >= info.goalCount) {
                            bucketItemImage.visibility = View.GONE
                            bucketItemLayout.isClickable = true
                        }

                    }, 900)
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
        successImageView.backgroundTintList = context.getColorStateList(R.color._a6c6ff)
    }


    open fun setFinalSuccessUIBackground() {
        successImageView.setBackgroundResource(R.drawable.check_complete)
        successImageView.backgroundTintList = context.getColorStateList(R.color._ffffff)
        bucketTitle.setTextColor(context.resources.getColor(R.color._ffffff))
        circularProgressBar.visibility = View.GONE
        bucketItemLayout.layoutParams.height = context.resources.getDimension(R.dimen.minimumBucketItemHeight).toInt()

        bucketItemImage.setBackgroundResource(R.drawable.bucket_item_success_background)
    }

    open fun setDoneSuccessUIButton() {
        successImageView.backgroundTintList = context.getColorStateList(R.color._e8e8e8)
        bucketItemImage.background = context.getDrawable(R.drawable.bucket_item_base_background)
    }

    open fun setUI(bucketItemInfo: BucketItem, bucketListener: View.OnClickListener) {
    }

    open fun createOnClickBucketSuccessListener(
            tokenId: String, bucketItemInfo: BucketItem): View.OnClickListener {
        return View.OnClickListener {
            bucketSuccess(bucketItemInfo)
        }
    }

    private fun bucketSuccess(bucketItemInfo: BucketItem) {
        val viewModel = BucketDetailViewModel()
        viewModel.setBucketComplete(object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                bucketSuccess(bucketItemInfo)
            }

            override fun start() {
                bucketItemLayout.isEnabled = false
            }

            override fun success() {
                onBucketSuccessFinalButtonClickListener(bucketItemInfo)
                bucketItemLayout.isEnabled = true
                return
            }

            override fun fail() {
                bucketItemLayout.isEnabled = true
                Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }

        }, bucketItemInfo.id)

    }

    open fun createOnClickBucketSuccessLayoutListener(tokenId: String, bucketItemInfo: BucketItem): View.OnClickListener {
        return View.OnClickListener {
            createOnClickBucketSuccessListener(tokenId, bucketItemInfo)
        }
    }

    open fun setFinalSuccessWithCountBucket() {}
    open fun addBucketSuccessCount() {}
    open fun setDdayColor() {}
}