package womenproject.com.mybury.presentation.mypage.dday

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.ItemDdayBucketSucceedBinding

class DdaySucceedBucketItemViewHolder(private val binding: ItemDdayBucketSucceedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(bucketListener: View.OnClickListener, bucketItemInfo: BucketItem) {
        binding.apply {

            setUI(bucketItemInfo, bucketListener)
            executePendingBindings()
        }
    }

    private fun setUI(bucketItemInfo: BucketItem, bucketListener: View.OnClickListener) {
        binding.bucketTitleText = bucketItemInfo.title
        binding.bucketClickListener = bucketListener
    }


}