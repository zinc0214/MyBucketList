package womenproject.com.mybury.presentation.main.bucketlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketType
import womenproject.com.mybury.databinding.ItemBucketDoingSimpleBinding
import womenproject.com.mybury.databinding.ItemBucketSucceedBinding
import womenproject.com.mybury.presentation.detail.BucketDetailViewModel
import womenproject.com.mybury.presentation.main.MainFragmentDirections


/**
 * Created by HanAYeon on 2018. 11. 27..
 */

open class MainBucketListAdapter(
    val viewModel: BucketDetailViewModel,
    private val showSnackBar: ((BucketItem) -> Unit)
) : RecyclerView.Adapter<ViewHolder>() {

    private  var bucketList: List<BucketItem> = emptyList()

    override fun getItemViewType(position: Int): Int {
        return bucketList[position].bucketType().int()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            BucketType.SUCCEED_ITEM.int() -> SucceedBucketItemViewHolder(
                ItemBucketSucceedBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> BaseBucketItemViewHolder(
                ItemBucketDoingSimpleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), showSnackBar
            )
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SucceedBucketItemViewHolder -> {
                holder.bind(createOnClickBucketListener(bucketList[position]), bucketList[position])
            }
            is BaseBucketItemViewHolder -> {
                holder.bind(
                    bucketItemInfo = bucketList[position],
                    isForDday = false,
                    viewModel = viewModel,
                    bucketListener = createOnClickBucketListener(bucketList[position])
                )
            }
        }
    }

    open fun createOnClickBucketListener(bucket: BucketItem): View.OnClickListener {

        return View.OnClickListener {
            val directions = MainFragmentDirections.actionMainBucketToBucketDetail()
            directions.bucketId = bucket.id
            it.findNavController().navigate(directions)
        }
    }

    override fun getItemCount(): Int {
        return bucketList.size
    }

    fun updateBucketList(bucketList: List<BucketItem>,) {
        this.bucketList = bucketList
        notifyDataSetChanged()
    }

}
