package womenproject.com.mybury.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.databinding.BucketItemBaseBinding
import womenproject.com.mybury.databinding.BucketItemCountBinding
import androidx.navigation.findNavController
import womenproject.com.mybury.view.DdayBucketListFragmentDirections
import womenproject.com.mybury.viewholder.DdayCountBucketItemViewHolder
import womenproject.com.mybury.viewholder.DdayNormalBucketItemViewHolder

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketEachListAdapter(context: Context?, bucketList: BucketList) : BaseBucketListAdapter(context, bucketList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        Log.e("ayhan:ViewType", "$viewType")

        currentViewHolder = when(viewType) {
            1 -> DdayNormalBucketItemViewHolder(BucketItemBaseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> DdayCountBucketItemViewHolder(BucketItemCountBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        return currentViewHolder
    }

    override fun createOnClickBucketListener(bucketId: Int): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(context, "count : $bucketId", Toast.LENGTH_SHORT).show()

            val directions = DdayBucketListFragmentDirections.ActionDdayBucketToBucketDetail(bucketId.toString())
            it.findNavController().navigate(directions)
        }
    }
}