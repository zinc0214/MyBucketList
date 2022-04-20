package womenproject.com.mybury.presentation.mypage.dday

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.ItemDdayBucketSucceedBinding
import womenproject.com.mybury.presentation.main.bucketlist.BucketItemHandler

class DdaySucceedBucketItemViewHolder(private val binding: ItemDdayBucketSucceedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(bucketItemHandler: BucketItemHandler, bucketItemInfo: BucketItem) {
        binding.apply {
            setUI(bucketItemHandler, bucketItemInfo)
            executePendingBindings()
        }
    }

    private fun setUI(bucketItemHandler: BucketItemHandler, bucketItemInfo: BucketItem) {
        binding.bucketTitleText = bucketItemInfo.title
        binding.bucketClickListener = View.OnClickListener {
            bucketItemHandler.bucketSelect(bucketItemInfo)
        }
    }
}