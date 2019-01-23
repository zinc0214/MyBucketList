package womenproject.com.mybury.adapter

import androidx.recyclerview.widget.DiffUtil
import womenproject.com.mybury.data.BucketList

/**
 * Created by HanAYeon on 2018. 11. 27..
 */

class BucketListDiffCallback : DiffUtil.ItemCallback<BucketList>() {

    override fun areItemsTheSame(oldItem: BucketList, newItem: BucketList): Boolean {
        return oldItem.list == newItem.list
    }

    override fun areContentsTheSame(oldItem: BucketList, newItem: BucketList): Boolean {
        return oldItem == newItem
    }
}