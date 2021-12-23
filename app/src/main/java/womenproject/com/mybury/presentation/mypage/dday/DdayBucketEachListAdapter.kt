package womenproject.com.mybury.presentation.mypage.dday

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketType
import womenproject.com.mybury.databinding.ItemBucketDoingSimpleBinding
import womenproject.com.mybury.databinding.ItemDdayBucketSucceedBinding
import womenproject.com.mybury.presentation.main.bucketlist.BaseBucketItemViewHolder

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketEachListAdapter(
    val bucketList: List<BucketItem>,
    private val showSnackBar: ((BucketItem) -> Unit)
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
                ), showSnackBar
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BaseBucketItemViewHolder -> {
                holder.bind(
                    createOnClickBucketListener(bucketList[position]),
                    bucketList[position],
                    true
                )
            }
            is DdaySucceedBucketItemViewHolder -> {
                holder.bind(createOnClickBucketListener(bucketList[position]), bucketList[position])
            }
        }
    }

    private fun createOnClickBucketListener(bucket: BucketItem): View.OnClickListener {
        return View.OnClickListener {
            val directions = DdayBucketListFragmentDirections.actionDdayBucketToBucketDetail()
            directions.bucketId = bucket.id
            it.findNavController().navigate(directions)
        }
    }

    override fun getItemCount(): Int {
        return bucketList.size
    }
}