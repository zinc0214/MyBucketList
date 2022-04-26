package womenproject.com.mybury.presentation.mypage.dday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.model.DdayBucketList
import womenproject.com.mybury.databinding.DdayBucketListBinding
import womenproject.com.mybury.presentation.main.bucketlist.BucketItemHandler

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketTotalListAdapter(
    private var bucketList: List<DdayBucketList>,
    private val bucketItemHandler: BucketItemHandler
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var currentViewHolderTotal: DdayBucketTotalListViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        currentViewHolderTotal = DdayBucketTotalListViewHolder(
            DdayBucketListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            bucketItemHandler
        )
        return currentViewHolderTotal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bucketList[position].isLast = position == bucketList.size - 1
        currentViewHolderTotal.apply {
            bind(bucketList[position])
        }
    }

    override fun getItemCount(): Int {
        return bucketList.size
    }

}
