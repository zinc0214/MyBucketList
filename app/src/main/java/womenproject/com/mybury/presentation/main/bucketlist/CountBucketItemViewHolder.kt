package womenproject.com.mybury.presentation.main.bucketlist

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Handler
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference.Companion.getShowDdayFilter
import womenproject.com.mybury.databinding.BucketItemCountBinding
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.detail.BucketDetailViewModel
import womenproject.com.mybury.ui.loadingbutton.animatedDrawables.ProgressType
import womenproject.com.mybury.ui.loadingbutton.customView.ProgressButton


class CountBucketItemViewHolder(
        private val isDdayUI: Boolean,
        private val binding: BucketItemCountBinding,
        private val showSnackBar: ((BucketItem) -> Unit)) : RecyclerView.ViewHolder(binding.root) {

    private val successImageView = binding.successButtonView.successImg
    private val circularProgressBar = binding.successButtonView.circularProgressBar

    private val animFadeOut = AnimationUtils.loadAnimation(MyBuryApplication.context, R.anim.fade_out)

    fun bind(bucketClickListener: View.OnClickListener, bucketInfo: BucketItem) {
        binding.apply {
            setUI(bucketClickListener, bucketInfo)
            executePendingBindings()
        }
    }

    private fun setUI(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem) {

        val countText: String = binding.root.context.resources.getString(R.string.bucket_count)
        val formattingCountText = Html.fromHtml(String.format(countText,
                "${bucketItemInfo.userCount}", "/${bucketItemInfo.goalCount}"))

        binding.userCount.text = formattingCountText
        binding.bucketTitleText = bucketItemInfo.title
        binding.horizontalProgressBar.progress = bucketItemInfo.userCount
        binding.horizontalProgressBar.max = bucketItemInfo.goalCount

        binding.bucketClickListener = bucketListener

        binding.successButtonView.bucketSuccessListener = createOnClickBucketSuccessListener(bucketItemInfo)
        binding.bucketSuccessClickListener = createOnClickBucketSuccessLayoutListener(bucketItemInfo)


        if (getShowDdayFilter(binding.root.context))  {
            bucketItemInfo.dDay?.run {
                binding.isOverDday = this < 0
                binding.ddayText = if (this < 0) "D${this.toString().replace("-","+")}" else "D-${this}"
            }
            binding.ddayTextView.visibility = if (bucketItemInfo.dDay != null) View.VISIBLE else View.INVISIBLE
        } else {
            binding.ddayTextView.visibility = View.GONE
        }

        if (isDdayUI) {
            val countText: String = binding.root.context.resources.getString(R.string.dday_bucket_count)
            val formattingCountText = Html.fromHtml(String.format(countText,
                    "${bucketItemInfo.userCount}", "/${bucketItemInfo.goalCount}"))
            binding.bucketItemLayout.background = MyBuryApplication.context.getDrawable(R.drawable.bucket_dday_click_background)
            binding.horizontalProgressBar.progressDrawable = MyBuryApplication.context.getDrawable(R.drawable.dday_horizontal_progressbar)
            binding.successButtonView.circularProgressBar.spinningBarColor = MyBuryApplication.context.getColor(R.color._ffca5a)
            binding.bucketSucceedImage.background = MyBuryApplication.context.getDrawable(R.drawable.shape_33cca248_r4)
            binding.userCount.text = formattingCountText
            binding.ddayTextView.visibility = View.GONE
        }
    }

    private fun onBucketSuccessFinalButtonClickListener(info: BucketItem) {
        binding.apply {
            circularProgressBar.run {
                progressType = ProgressType.INDETERMINATE
                startAnimation()
                progressAnimator(this).start()
                Handler().run {
                    bucketItemLayout.isClickable = false
                    postDelayed({
                        setFinalSuccessUIButton()
                        addBucketSuccessCount(info)
                    }, 500)
                    postDelayed({
                        revertAnimation()
                        if (info.userCount >= info.goalCount) {
                            // Nothing
                        } else {
                            setDoneSuccessUIButton()
                            bucketItemLayout.isClickable = true
                        }
                    }, 750)
                    postDelayed({
                        if (info.userCount >= info.goalCount) {
                            animFadeOut.duration = 750
                            bucketItemImage.startAnimation(animFadeOut)
                        }
                    }, 800)
                    postDelayed({
                        if (info.userCount >= info.goalCount) {
                            bucketItemLayout.isClickable = true
                            setFinalSuccessUIBackground()
                            setFinalSuccessWithCountBucket()
                        }
                    }, 1000)
                    postDelayed({
                        bucketSuccess(info)
                    }, 1100)
                }
            }
            executePendingBindings()
        }
    }


    private fun progressAnimator(progressButton: ProgressButton) = ValueAnimator.ofFloat(0F, 100F).apply {
        duration = 700
        startDelay = 0
        addUpdateListener { animation ->
            progressButton.setProgress(animation.animatedValue as Float)
        }
        if (isDdayUI) {
            binding.bucketItemImage.setBackgroundResource(R.drawable.shape_ffffff_r4_strk_13_ffca5a)
        } else {
            binding.bucketItemImage.setBackgroundResource(R.drawable.shape_ffffff_r4_strk_13_a6c6ff)
        }
    }

    private fun setFinalSuccessUIButton() {
        if (isDdayUI) {
            successImageView.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color._ffca5a)
        } else {
            successImageView.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color._a6c6ff)
        }

    }

    private fun setFinalSuccessUIBackground() {
        successImageView.setBackgroundResource(R.drawable.check_complete)
        successImageView.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color._ffffff)
        circularProgressBar.visibility = View.GONE

        if (isDdayUI) {
            binding.bucketItemImage.setBackgroundResource(R.drawable.shape_ffca5a_r4)
        } else {
            binding.bucketItemImage.setBackgroundResource(R.drawable.shape_a6c6ff_r4)
        }
    }

    private fun setProgressMax(pb: ProgressBar, max: Int) {
        pb.max = max * 100
    }

    private fun setDoneSuccessUIButton() {
        successImageView.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color._e8e8e8)
        binding.bucketItemImage.background = MyBuryApplication.context.getDrawable(R.drawable.shape_ffffff_r4_strk_06_e8e8e8)
    }


    private fun createOnClickBucketSuccessListener(bucketItemInfo: BucketItem): View.OnClickListener {
        return View.OnClickListener {
            onBucketSuccessFinalButtonClickListener(bucketItemInfo)

        }
    }

    private fun bucketSuccess(bucketItemInfo: BucketItem) {
        val viewModel = BucketDetailViewModel()
        viewModel.setBucketComplete(object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                bucketSuccess(bucketItemInfo)
            }

            override fun start() {
                binding.bucketItemLayout.isEnabled = false
            }

            override fun success() {
                binding.bucketItemLayout.isEnabled = true
                showSnackBar.invoke(bucketItemInfo)
                return
            }

            override fun fail() {
                binding.bucketItemLayout.isEnabled = true
                Toast.makeText(MyBuryApplication.context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }

        }, bucketItemInfo.id)

    }

    private fun createOnClickBucketSuccessLayoutListener(bucketItemInfo: BucketItem): View.OnClickListener {
        return View.OnClickListener {
            createOnClickBucketSuccessListener(bucketItemInfo)
        }
    }

    private fun addBucketSuccessCount(info: BucketItem) {

        val formattingCountText: Spanned
        formattingCountText = if (isDdayUI) {
            val countText: String = binding.root.context.resources.getString(R.string.dday_bucket_count)
            Html.fromHtml(String.format(countText,
                    "${info.userCount + 1}", "/${info.goalCount}"))
        } else {
            val countText: String = binding.root.context.resources.getString(R.string.bucket_count)
            Html.fromHtml(String.format(countText,
                    "${info.userCount + 1}", "/${info.goalCount}"))
        }
        binding.userCount.text = formattingCountText
        binding.horizontalProgressBar.progress = info.userCount + 1
        info.userCount += 1
        setProgressMax(binding.horizontalProgressBar, info.goalCount)
        setProgressAnimate(binding.horizontalProgressBar, info.userCount)
    }

    private fun setFinalSuccessWithCountBucket() {
        binding.prgressLayout.visibility = View.GONE
        binding.successButtonLayout.visibility = View.GONE
        binding.ddayTextView.visibility = View.INVISIBLE
        binding.executePendingBindings()
    }

    private fun setProgressAnimate(pb: ProgressBar, progressTo: Int) {
        val animation = ObjectAnimator.ofInt(pb, "progress",
                (progressTo - 1) * 100, progressTo * 100)
        animation.duration = 1000
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }

}