package womenproject.com.mybury.presentation.mypage.dday

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.DdayBucketList
import womenproject.com.mybury.databinding.DdayBucketListBinding
import womenproject.com.mybury.presentation.detail.BucketDetailViewModel

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketTotalListViewHolder(
    private val binding: DdayBucketListBinding,
    private val showSnackBar: ((BucketItem) -> Unit)
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(bucketItemList: DdayBucketList, viewModel: BucketDetailViewModel, context: Context) {
        binding.apply {
            bucketItemList.day.apply {
                isOverDday = this < 0
                ddayText = if (this < 0) "D${this.toString().replace("-", "+")}" else "D-${this}"
            }

            ddayEachBucketItemList.layoutManager = LinearLayoutManager(context)
            ddayEachBucketItemList.hasFixedSize()
            ddayEachBucketItemList.adapter =
                DdayBucketEachListAdapter(bucketItemList.bucketlists, viewModel, showSnackBar)

            executePendingBindings()
        }
    }
}