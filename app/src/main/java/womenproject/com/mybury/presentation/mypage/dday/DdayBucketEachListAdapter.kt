package womenproject.com.mybury.presentation.mypage.dday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.model.BucketType
import womenproject.com.mybury.databinding.ItemBucketDoingSimpleBinding
import womenproject.com.mybury.databinding.ItemDdayBucketSucceedBinding
import womenproject.com.mybury.presentation.main.bucketlist.BaseBucketItemViewHolder
import womenproject.com.mybury.presentation.main.bucketlist.BucketItemHandler

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketEachListAdapter(
    val bucketList: List<BucketItem>,
    private val bucketItemHandler: BucketItemHandler
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        return bucketList[position].bucketType().int()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            BucketType.SUCCEED_ITEM.int() -> DdaySucceedBucketItemViewHolder(
                ItemDdayBucketSucceedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> BaseBucketItemViewHolder(
                ItemBucketDoingSimpleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BaseBucketItemViewHolder -> {
                holder.bind(
                    bucketItemInfo = bucketList[position],
                    isShowDday = true,
                    isForDday = true,
                    bucketItemHandler = bucketItemHandler
                )
            }
            is DdaySucceedBucketItemViewHolder -> {
                holder.bind(bucketItemHandler, bucketList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return bucketList.size
    }
}