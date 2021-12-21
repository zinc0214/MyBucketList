package womenproject.com.mybury.presentation.main.bucketlist

import android.animation.ObjectAnimator
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.item_bucket_succeed.view.success_img
import kotlinx.android.synthetic.main.widget_bucket_done_button.view.*
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.databinding.ItemBucketDoingCountBinding

open class CountBucketItemViewHolder(
    private val isDday: Boolean,
    private val binding: ItemBucketDoingCountBinding,
    showSnackBar: ((BucketItem) -> Unit)
) : BaseBucketItemViewHolder(binding, showSnackBar) {

    init {
        bucketItemLayout = binding.bucketItemLayout
        successImageView = binding.successButtonLayout.success_img
        bucketItemImage = binding.bucketItemImage
        bucketTitle = binding.bucketTitle
        circularProgressBar = binding.successButtonLayout.circular_progress_bar
        ddayCountView = binding.ddayTextView
    }

    override fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem) {
        binding.apply {
            setUI(bucketItemInfo, bucketListener)
            executePendingBindings()
        }
    }

    override fun setUI(bucketItemInfo: BucketItem, bucketListener: View.OnClickListener) {
        binding.bucketInfo = bucketItemInfo
        binding.isDdayVisislbe =
            Preference.getShowDdayFilter(binding.root.context) && bucketItemInfo.dDay != null
        binding.successButtonView.bucketSuccessListener =
            createOnClickBucketSuccessListener(bucketItemInfo)
        binding.bucketClickListener = bucketListener
        binding.userCount.text = setCountText(bucketItemInfo.userCount, bucketItemInfo.goalCount)
    }


    override fun addBucketSuccessCount(info: BucketItem) {
        binding.userCount.text = setCountText(info.userCount + 1, info.goalCount)
        binding.horizontalProgressBar.progress = info.userCount + 1
        info.userCount += 1
        setProgressMax(binding.horizontalProgressBar, info.goalCount)
        setProgressAnimate(binding.horizontalProgressBar, info.userCount)
    }

    override fun setFinalSuccessUIBackground() {
        super.setFinalSuccessUIBackground()
        binding.progressLayout.visibility = View.GONE
    }

    private fun setCountText(userCount: Int, goalCount: Int): Spanned {
        val countText: String = if (isDday) {
            binding.root.context.resources.getString(R.string.dday_bucket_count)
        } else {
            binding.root.context.resources.getString(R.string.bucket_count)
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