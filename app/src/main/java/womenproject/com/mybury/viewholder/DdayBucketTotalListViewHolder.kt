package womenproject.com.mybury.viewholder

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.adapter.DdayBucketEachListAdapter
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.DdayEachBucketGroup
import womenproject.com.mybury.databinding.DdayBucketListBinding

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketTotalListViewHolder(private val binding: DdayBucketListBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ddayEachBucketGroup: DdayEachBucketGroup, context: Context) {
        binding.apply {
            ddayText = "D-${ddayEachBucketGroup.dday}"

            val bucketList = BucketList(ddayEachBucketGroup.ddayBucketItemList as ArrayList<BucketItem>)
            ddayEachBucketItemList.layoutManager = LinearLayoutManager(context)
            ddayEachBucketItemList.hasFixedSize()
            ddayEachBucketItemList.adapter =  DdayBucketEachListAdapter(context, bucketList)

            executePendingBindings()
        }
    }
}