package womenproject.com.mybury.presentation.main.bucketlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketType
import womenproject.com.mybury.databinding.ItemBucketDoingSimpleBinding
import womenproject.com.mybury.databinding.ItemBucketSucceedBinding


/**
 * Created by HanAYeon on 2018. 11. 27..
 */

class MainBucketListAdapter(
    private var bucketList: List<BucketItem>,
    private val bucketItemHandler: BucketItemHandler
) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return bucketList[position].bucketType().int()
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
                holder.bind(bucketItemHandler, bucketList[position])
            }
            is BaseBucketItemViewHolder -> {
                holder.bind(
                    bucketItemInfo = bucketList[position],
                    isForDday = false,
                    bucketItemHandler = bucketItemHandler
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return bucketList.size
    }
}
