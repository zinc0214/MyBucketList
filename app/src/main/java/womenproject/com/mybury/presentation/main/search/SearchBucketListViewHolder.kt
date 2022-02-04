package womenproject.com.mybury.presentation.main.search

import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.databinding.ItemSearchBucketBinding
import womenproject.com.mybury.util.Converter

class SearchBucketListViewHolder(private val binding: ItemSearchBucketBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        bucketListener: View.OnClickListener,
        bucketItemInfo: BucketItem,
        isForDday: Boolean = false,
        isShowDday: Boolean = false,
        isLastItem: Boolean = false
    ) {
        if (isForDday) {
            setDdayColor()
        }
        setUI(bucketItemInfo, bucketListener, isForDday, isShowDday, isLastItem)
        binding.executePendingBindings()
    }

    private fun setUI(
        bucketItemInfo: BucketItem,
        bucketListener: View.OnClickListener,
        isForDday: Boolean,
        isShowDday: Boolean,
        isLastItem: Boolean
    ) {
        binding.bucketInfo = bucketItemInfo
        binding.bucketClickListener = bucketListener
        if (Preference.getShowDdayFilter(binding.root.context) || isShowDday) {
            binding.ddayTextView.visibility =
                if (bucketItemInfo.dDay != null) View.VISIBLE else View.INVISIBLE
        } else {
            binding.ddayTextView.visibility = View.GONE
        }

        binding.userCount.text =
            setCountText(bucketItemInfo.userCount, bucketItemInfo.goalCount, isForDday)

        if (isLastItem) {
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(Converter.dpToPx(8))
            binding.bucketItemLayout.layoutParams = lp
        }
    }

    private fun setDdayColor() {
        binding.bucketItemImage.setBackgroundResource(R.drawable.bucket_dday_click_background)
        binding.bucketSucceedImage.setBackgroundResource(R.drawable.shape_efefef_r4_strk_e8e8e8)
        binding.horizontalProgressBar.progressDrawable =
            binding.root.context.getDrawable(R.drawable.dday_horizontal_progressbar)
        binding.ddayTextView.visibility = View.GONE
    }

    private fun setCountText(userCount: Int, goalCount: Int, isForDday: Boolean): Spanned {
        val countText: String = if (isForDday) {
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
}