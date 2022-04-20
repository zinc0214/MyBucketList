package womenproject.com.mybury.presentation.mypage.dday

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.DdayBucketList
import womenproject.com.mybury.databinding.DdayBucketListBinding
import womenproject.com.mybury.presentation.detail.BucketDetailViewModel

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketTotalListAdapter(
    context: Context?,
    val bucketList: List<DdayBucketList>,
    val viewModel: BucketDetailViewModel,
    private val showSnackBar: ((BucketItem) -> Unit)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var context: Context = context!!
    private lateinit var currentViewHolderTotal: DdayBucketTotalListViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        currentViewHolderTotal = DdayBucketTotalListViewHolder(
            DdayBucketListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            showSnackBar
        )
        return currentViewHolderTotal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bucketList[position].isLast = position == bucketList.size - 1
        currentViewHolderTotal.apply {
            bind(bucketList[position], viewModel, context)
        }
    }

    override fun getItemCount(): Int {
        return bucketList.size
    }

}
