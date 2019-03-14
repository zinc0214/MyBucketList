package womenproject.com.mybury.viewholder

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.R
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

            val bucketList = BucketList(ddayEachBucketGroup.ddayBucketItemList as ArrayList<BucketItem>, false, 200)
            ddayEachBucketItemList.layoutManager = LinearLayoutManager(context)
            ddayEachBucketItemList.hasFixedSize()
            ddayEachBucketItemList.adapter =  DdayBucketEachListAdapter(context, bucketList)

            Log.e("ayhan",  "${ddayEachBucketGroup.isLast}")
            lastEndImg.endImage.background = context.getDrawable(R.drawable.end_character_d_day)
            lastEndImg.lastImgVisible = if(ddayEachBucketGroup.isLast) {
                View.VISIBLE
            } else {
                View.GONE
            }
            executePendingBindings()
        }
    }
}