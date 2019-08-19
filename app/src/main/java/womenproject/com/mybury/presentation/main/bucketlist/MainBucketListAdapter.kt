package womenproject.com.mybury.presentation.main.bucketlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import womenproject.com.mybury.presentation.base.BaseBucketListAdapter
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.databinding.BucketItemBaseBinding
import womenproject.com.mybury.databinding.BucketItemCountBinding
import womenproject.com.mybury.databinding.BucketItemSucceedBinding
import womenproject.com.mybury.presentation.util.BaseCountBucketItemViewHolder


/**
 * Created by HanAYeon on 2018. 11. 27..
 */

class MainBucketListAdapter(context: Context?, bucketList: BucketList) : BaseBucketListAdapter(context, bucketList) {

    override fun getItemViewType(position: Int): Int {
        return checkBucketType(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        currentViewHolder = when (viewType) {
            0 -> SucceedBucketItemViewHolder(BucketItemSucceedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            1 -> BaseNormalBucketItemViewHolder(BucketItemBaseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> BaseCountBucketItemViewHolder(BucketItemCountBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
        return currentViewHolder
    }

    private fun checkBucketType(position: Int): Int {
        return if (bucketItemList.bucketlists[position].complete) {
            0
        } else {
            bucketItemList.bucketlists[position].goalCount
        }

    }
}
