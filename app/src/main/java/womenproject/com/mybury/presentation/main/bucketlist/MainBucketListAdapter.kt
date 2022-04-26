package womenproject.com.mybury.presentation.main.bucketlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.model.BucketType
import womenproject.com.mybury.databinding.ItemBucketDoingSimpleBinding
import womenproject.com.mybury.databinding.ItemBucketSucceedBinding


/**
 * Created by HanAYeon on 2018. 11. 27..
 */

class MainBucketListAdapter(
    private val bucketItemHandler: BucketItemHandler
) : ListAdapter<BucketItem, ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).bucketType().int()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            BucketType.SUCCEED_ITEM.int() -> SucceedBucketItemViewHolder(
                ItemBucketSucceedBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> BaseBucketItemViewHolder(
                ItemBucketDoingSimpleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SucceedBucketItemViewHolder -> {
                holder.bind(bucketItemHandler, getItem(position))
            }
            is BaseBucketItemViewHolder -> {
                holder.bind(
                    bucketItemInfo = getItem(position),
                    isForDday = false,
                    bucketItemHandler = bucketItemHandler
                )
            }
        }
    }

    fun replaceItems(items: List<BucketItem>) {
        submitList(items)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<BucketItem>() {
            override fun areContentsTheSame(oldItem: BucketItem, newItem: BucketItem) =
                oldItem.userCount == newItem.userCount

            override fun areItemsTheSame(oldItem: BucketItem, newItem: BucketItem) =
                oldItem == newItem
        }
    }
}
