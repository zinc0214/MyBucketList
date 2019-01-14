package womenproject.com.mybury.viewholder

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem

/**
 * Created by HanAYeon on 2019. 1. 10..
 */

abstract class BaseBucketItemViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(bucketListener: View.OnClickListener, bucektItemInfo : BucketItem, context: Context)

}