package womenproject.com.mybury.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.DdayTotalBucketList
import womenproject.com.mybury.databinding.DdayBucketListBinding
import womenproject.com.mybury.viewholder.DdayBucketTotalListViewHolder

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketTotalListAdapter(context: Context?, ddayBucketTotalList: DdayTotalBucketList) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var context: Context = context!!
    private var ddayBucketTotalList = ddayBucketTotalList
    private lateinit var currentViewHolderTotal: DdayBucketTotalListViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        Log.e("ayhan:ViewType", "$viewType")

        currentViewHolderTotal = DdayBucketTotalListViewHolder(DdayBucketListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return currentViewHolderTotal

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val holder = currentViewHolderTotal
        holder.apply {
            bind(ddayBucketTotalList.ddayBucketEachListItem[position], context)
        }
    }


    override fun getItemCount(): Int {
        return ddayBucketTotalList.ddayBucketEachListItem.size
    }

}
