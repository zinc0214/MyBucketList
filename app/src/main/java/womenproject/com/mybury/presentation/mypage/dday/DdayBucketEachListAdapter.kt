package womenproject.com.mybury.presentation.mypage.dday

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BASE_ITEM
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.COUNT_ITEM
import womenproject.com.mybury.data.SUCCEED_ITEM
import womenproject.com.mybury.databinding.BucketItemBaseBinding
import womenproject.com.mybury.databinding.BucketItemCountBinding
import womenproject.com.mybury.databinding.DdayBucketItemSucceedBinding
import womenproject.com.mybury.presentation.main.bucketlist.CountBucketItemViewHolder

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketEachListAdapter(val bucketList: List<BucketItem>, private val showSnackBar: ((BucketItem) -> Unit)) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        Log.e(this.toString(), "getItemViewType is Use")
        return checkBucketType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            SUCCEED_ITEM -> DdaySucceedBucketItemViewHolder(DdayBucketItemSucceedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            COUNT_ITEM -> CountBucketItemViewHolder(true, BucketItemCountBinding.inflate(LayoutInflater.from(parent.context), parent, false), showSnackBar)
            else -> DdayNormalBucketItemViewHolder(BucketItemBaseBinding.inflate(LayoutInflater.from(parent.context), parent, false), showSnackBar)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DdayNormalBucketItemViewHolder -> {
                holder.bind(createOnClickBucketListener(bucketList[position]), bucketList[position])
            }
            is CountBucketItemViewHolder -> {
                holder.bind(createOnClickBucketListener(bucketList[position]), bucketList[position])
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

    private fun checkBucketType(position: Int): Int {
        return when {
            bucketList[position].userCount >= bucketList[position].goalCount -> SUCCEED_ITEM
            bucketList[position].goalCount > 1 -> COUNT_ITEM
            else -> BASE_ITEM
        }
    }


    override fun getItemCount(): Int {
        return bucketList.size
    }
}