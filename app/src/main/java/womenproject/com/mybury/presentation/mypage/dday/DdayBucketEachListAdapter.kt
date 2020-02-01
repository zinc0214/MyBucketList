package womenproject.com.mybury.presentation.mypage.dday

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.BucketItemBaseBinding
import womenproject.com.mybury.databinding.BucketItemCountBinding
import womenproject.com.mybury.presentation.main.bucketlist.CountBucketItemViewHolder

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketEachListAdapter(val bucketList: List<BucketItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        Log.e(this.toString(), "getItemViewType is Use")
        return checkBucketType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            1 -> DdayNormalBucketItemViewHolder(BucketItemBaseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> CountBucketItemViewHolder(true, BucketItemCountBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
        return if (bucketList[position].userCount == bucketList[position].goalCount) {
            0
        } else {
            bucketList[position].goalCount
        }
    }

    override fun getItemCount(): Int {
        return bucketList.size
    }
}