package womenproject.com.mybury.presentation.main.bucketlist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import womenproject.com.mybury.data.*
import womenproject.com.mybury.databinding.BucketItemBaseBinding
import womenproject.com.mybury.databinding.BucketItemCountBinding
import womenproject.com.mybury.databinding.BucketItemSucceedBinding
import womenproject.com.mybury.presentation.main.MainFragmentDirections


/**
 * Created by HanAYeon on 2018. 11. 27..
 */

open class MainBucketListAdapter(val bucketList: List<BucketItem>) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return checkBucketType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            SUCCEED_ITEM -> SucceedBucketItemViewHolder(BucketItemSucceedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            COUNT_ITEM -> CountBucketItemViewHolder(NORMAL, BucketItemCountBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> BaseNormalBucketItemViewHolder(BucketItemBaseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    private fun checkBucketType(position: Int): Int {
        return when {
            bucketList[position].userCount >= bucketList[position].goalCount -> SUCCEED_ITEM
            bucketList[position].goalCount > 1 -> COUNT_ITEM
            else -> BASE_ITEM
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder) {
            is SucceedBucketItemViewHolder -> {
                holder.bind(createOnClickBucketListener(bucketList[position]), bucketList[position])
            }
            is CountBucketItemViewHolder -> {
                holder.bind(createOnClickBucketListener(bucketList[position]), bucketList[position])
            }
            is BaseBucketItemViewHolder -> {
                holder.bind(createOnClickBucketListener(bucketList[position]), bucketList[position])
            }
        }
    }

    open fun createOnClickBucketListener(bucket: BucketItem): View.OnClickListener {

        return View.OnClickListener {
            val directions = MainFragmentDirections.actionMainBucketToBucketDetail()
            directions.bucketId = bucket.id
            it.findNavController().navigate(directions)
        }
    }

    override fun getItemCount(): Int {
        Log.e("ayhan", "size : ${bucketList.size}")
        return bucketList.size
    }


}
