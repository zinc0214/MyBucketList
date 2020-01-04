package womenproject.com.mybury.presentation.main.bucketlist

import android.view.View
import womenproject.com.mybury.MyBuryApplication.Companion.context
import womenproject.com.mybury.data.BucketItem
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
    }


}