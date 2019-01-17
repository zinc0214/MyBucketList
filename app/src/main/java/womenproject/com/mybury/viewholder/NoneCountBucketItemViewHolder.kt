package womenproject.com.mybury.viewholder

import android.content.Context
import android.view.View
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.BucketItemBaseBinding
import womenproject.com.mybury.util.loadingbutton.customView.CircularProgressButton

/**
 * Created by HanAYeon on 2019. 1. 9..
 */

class NoneCountBucketItemViewHolder(private val binding: BucketItemBaseBinding) : BaseBucketItemViewHolder(binding) {


    init {
        bucketItemLayout = binding.bucketItemLayout
        successImageView = binding.successImg
        bucketItemImage = binding.bucketItemImage
        bucketTitle = binding.bucketTitle
        circularProgressBar = binding.circularProgressBar
    }

    override fun bind(bucketListener: View.OnClickListener, bucketItemInfo : BucketItem, context : Context) {
        binding.apply {
            bucketDDay = bucketItemInfo.dday
            if(bucketItemInfo.dday == 1) {
                bucketItemImage.background = context.getDrawable(R.drawable.bucket_item_dday_background)
            }
            bucketClickListener = bucketListener
            bucketSuccessListener = createOnClickBucketSuccessListener()
            bucketTitleText = bucketItemInfo.title
            executePendingBindings()
        }
    }

    private fun createOnClickBucketSuccessListener(): View.OnClickListener {
        return View.OnClickListener {
            onBucketSuccessFinalButtonClickListener()
        }
    }

    override fun addCurrentValue() {

    }
}