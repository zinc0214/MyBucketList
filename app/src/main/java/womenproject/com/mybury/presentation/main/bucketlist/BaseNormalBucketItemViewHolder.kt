package womenproject.com.mybury.presentation.main.bucketlist

import android.util.Log
import android.view.View
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
import womenproject.com.mybury.databinding.BucketItemBaseBinding

/**
 * Created by HanAYeon on 2019. 1. 9..
 */

open class BaseNormalBucketItemViewHolder(private val binding: BucketItemBaseBinding) : BaseBucketItemViewHolder(binding) {

    init {
        bucketItemLayout = binding.bucketItemLayout
        successImageView = binding.successButtonLayout.successImg
        bucketItemImage = binding.bucketItemImage
        bucketTitle = binding.bucketTitle
        circularProgressBar = binding.successButtonLayout.circularProgressBar
    }

    override fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem) {
        binding.apply {
            setUI(bucketItemInfo, bucketListener)
            setDdayColor()
            executePendingBindings()
        }
    }

    override fun setUI(bucketItemInfo: BucketItem, bucketListener: View.OnClickListener) {
        super.setUI(bucketItemInfo, bucketListener)

        binding.bucketTitleText = bucketItemInfo.title

        binding.bucketClickListener = bucketListener

        val tokenId = getAccessToken(context)
        binding.successButtonLayout.bucketSuccessListener = createOnClickBucketSuccessListener(tokenId, bucketItemInfo)
        binding.bucketSuccessClickListener = createOnClickBucketSuccessLayoutListener(tokenId, bucketItemInfo)

        Log.e("ayhan", "base dday :${bucketItemInfo.dDay} ")
        if (Preference.getShowDdayFilter(binding.root.context))  {
            bucketItemInfo.dDay?.run {
                Log.e("ayhan", "base dday info:${bucketItemInfo.dDay} ")
                binding.isOverDday = this < 0
                binding.ddayText = if (this < 0) "D${this.toString().replace("-","+")}" else "D-${this}"
            }
            binding.ddayTextView.visibility = if (bucketItemInfo.dDay != null) View.VISIBLE else View.INVISIBLE
        } else {
            binding.ddayTextView.visibility = View.GONE
        }

    }


}