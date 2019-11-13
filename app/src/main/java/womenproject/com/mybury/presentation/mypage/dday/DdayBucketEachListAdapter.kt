package womenproject.com.mybury.presentation.mypage.dday

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
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.presentation.base.BaseBucketListAdapter
import womenproject.com.mybury.presentation.mypage.dday.DdayCountBucketItemViewHolder
import womenproject.com.mybury.presentation.mypage.dday.DdayNormalBucketItemViewHolder

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketEachListAdapter(context: Context?, bucketList: List<BucketItem>) : BaseBucketListAdapter(context, bucketList) {

    override fun getItemViewType(position: Int): Int {
        Log.e(this.toString(), "getItemViewType is Use")
        return checkBucketType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        Log.e("ayhan:ViewType_dday", "$viewType")

        currentViewHolder = when(viewType) {
            1 -> DdayNormalBucketItemViewHolder(BucketItemBaseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> DdayCountBucketItemViewHolder(BucketItemCountBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        return currentViewHolder
    }

    override fun createOnClickBucketListener(bucket: BucketItem): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(context, "count : ${bucket.title}", Toast.LENGTH_SHORT).show()
            val directions = DdayBucketListFragmentDirections.actionDdayBucketToBucketDetail()
            directions.bucket = bucket
            it.findNavController().navigate(directions)
        }
    }

    private fun checkBucketType(position: Int): Int {
        return if(bucketItemList[position].complete) {
            0
        } else {
            bucketItemList[position].goalCount
        }

    }

}