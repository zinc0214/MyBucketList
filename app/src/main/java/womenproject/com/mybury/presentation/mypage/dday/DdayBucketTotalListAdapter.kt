package womenproject.com.mybury.presentation.mypage.dday

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.*
import womenproject.com.mybury.databinding.DdayBucketListBinding

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketTotalListAdapter(context: Context?, val bucketList: List<DdayBucketList>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var context: Context = context!!
    private lateinit var currentViewHolderTotal: DdayBucketTotalListViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        currentViewHolderTotal = DdayBucketTotalListViewHolder(DdayBucketListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return currentViewHolderTotal

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        bucketList[position].isLast = position == bucketList.size - 1
        currentViewHolderTotal.apply {
            bind(bucketList[position], context)
        }

    }


    override fun getItemCount(): Int {
        Log.e("ayhan", "bbbbb${bucketList.size}, ${bucketList.get(0).day}")
        return bucketList.size
    }

}
