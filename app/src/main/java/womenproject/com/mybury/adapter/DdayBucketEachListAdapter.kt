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
import womenproject.com.mybury.view.MainFragmentDirections
import womenproject.com.mybury.viewholder.CountBucketItemViewHolder
import womenproject.com.mybury.viewholder.NoneCountBucketItemViewHolder
import androidx.navigation.findNavController
import womenproject.com.mybury.view.DdayBucketListFragmentDirections

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketEachListAdapter(context: Context?, bucketList: BucketList) : BaseBucketListAdapter(context, bucketList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        Log.e("ayhan:ViewType", "$viewType")

        if (viewType == 1) {
            currentViewHolder = NoneCountBucketItemViewHolder(true, BucketItemBaseBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            return currentViewHolder as NoneCountBucketItemViewHolder
        } else {
            currentViewHolder = CountBucketItemViewHolder(true, BucketItemCountBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            return currentViewHolder as CountBucketItemViewHolder
        }

    }

    override fun createOnClickBucketListener(bucketId: Int): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(context, "count : $bucketId", Toast.LENGTH_SHORT).show()

            val directions = DdayBucketListFragmentDirections.ActionDdayBucketToBucketDetail(bucketId.toString())
            it.findNavController().navigate(directions)
        }
    }
}