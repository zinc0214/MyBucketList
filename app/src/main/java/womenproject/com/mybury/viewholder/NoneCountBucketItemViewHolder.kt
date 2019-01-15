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


    override fun bind(bucketListener: View.OnClickListener, bucketItemInfo : BucketItem, context : Context) {
        binding.apply {
            bucketDDay = bucketItemInfo.dday
            if(bucketItemInfo.dday == 1) {
                bucketItemLayout.background = context.getDrawable(R.drawable.bucket_item_dday_background)
            }
            bucketClickListener = bucketListener
            bucketSuccessListener = createOnClickBucketSuccessListener(circularProgressBar)
            bucketTitleText = bucketItemInfo.title
            executePendingBindings()
        }
    }

    private fun createOnClickBucketSuccessListener(circularProgressButton: CircularProgressButton): View.OnClickListener {
        return View.OnClickListener {
            onBucketSuccessFinalButtonClickListener(circularProgressButton, binding.bucketItemLayout)
        }
    }

    override fun setFinalSuccessUIButton() {
        binding.successImg.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color.bucket_success_btn_background)
        binding.bucketItemLayout.setBackgroundResource(R.drawable.bucket_item_successing_background)
    }

    override fun setFinalSuccessUIBackground() {
        binding.successImg.setBackgroundResource(R.drawable.check_complete)
        binding.successImg.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color.white)
        binding.bucketItemLayout.setBackgroundResource(R.drawable.bucket_item_success_background)
        binding.bucketTitle.setTextColor(MyBuryApplication.context.resources.getColor(R.color.white))
        binding.circularProgressBar.visibility = View.GONE
    }

    override fun setFinalSucceedUIBackground() {
        binding.bucketItemLayout.setBackgroundResource(R.drawable.bucket_item_succeed_background)
    }

    override fun setDoneSuccessUIButton() {
        binding.successImg.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color.bucket_base_btn_background)
        if(bucketDDay == 1) {
            binding.bucketItemLayout.background = MyBuryApplication.context.getDrawable(R.drawable.bucket_item_dday_background)
        } else {
            binding.bucketItemLayout.background = MyBuryApplication.context.getDrawable(R.drawable.bucket_item_base_background)
        }
    }

    override fun addCurrentValue() {

    }
}