package womenproject.com.mybury.presentation.mypage.dday

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.model.DdayBucketList
import womenproject.com.mybury.data.toBucketData
import womenproject.com.mybury.databinding.DdayBucketListBinding
import womenproject.com.mybury.presentation.main.bucketlist.BucketItemHandler

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketTotalListViewHolder(
    private val binding: DdayBucketListBinding,
    private val bucketItemHandler: BucketItemHandler
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(bucketItemList: DdayBucketList) {
        binding.apply {
            bucketItemList.day.apply {
                isOverDday = this < 0
                ddayText = if (this < 0) "D${this.toString().replace("-", "+")}" else "D-${this}"
            }

            ddayEachBucketItemList.layoutManager = LinearLayoutManager(binding.root.context)
            ddayEachBucketItemList.hasFixedSize()
            ddayEachBucketItemList.adapter =
                DdayBucketEachListAdapter(
                    bucketItemList.bucketLists.toBucketData(),
                    bucketItemHandler
                )

            executePendingBindings()
        }
    }
}