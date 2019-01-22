package womenproject.com.mybury.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.DdayEachBucketGroup
import womenproject.com.mybury.databinding.BucketItemBaseBinding
import womenproject.com.mybury.databinding.BucketItemCountBinding
import womenproject.com.mybury.databinding.BucketItemSucceedBinding
import womenproject.com.mybury.databinding.DdayBucketListBinding
import womenproject.com.mybury.viewholder.*

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
        } else if(viewType > 1){
            currentViewHolder = CountBucketItemViewHolder(true, BucketItemCountBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            return currentViewHolder as CountBucketItemViewHolder
        } else {
            currentViewHolder = SucceedBucketItemViewHolder(BucketItemSucceedBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            return currentViewHolder as SucceedBucketItemViewHolder
        }

    }

}