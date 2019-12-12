package womenproject.com.mybury.presentation.mypage.dday

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.R
import womenproject.com.mybury.data.DdayBucketList
import womenproject.com.mybury.databinding.DdayBucketListBinding

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketTotalListViewHolder(private val binding: DdayBucketListBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(bucketItemList: DdayBucketList, context: Context) {
        binding.apply {
            ddayText = "D-${bucketItemList.day}"

            ddayEachBucketItemList.layoutManager = LinearLayoutManager(context)
            ddayEachBucketItemList.hasFixedSize()
            ddayEachBucketItemList.adapter = DdayBucketEachListAdapter(context, bucketItemList.bucketlists)

            lastEndImg.endImage.background = context.getDrawable(R.drawable.end_character_d_day)
            lastEndImg.lastImgVisible = if (bucketItemList.isLast) {
                View.VISIBLE
            } else {
                View.GONE
            }
            executePendingBindings()
        }
    }
}