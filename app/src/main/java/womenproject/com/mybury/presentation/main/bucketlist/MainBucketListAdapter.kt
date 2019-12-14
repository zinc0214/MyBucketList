package womenproject.com.mybury.presentation.main.bucketlist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.BucketItemBaseBinding
import womenproject.com.mybury.databinding.BucketItemCountBinding
import womenproject.com.mybury.databinding.BucketItemSucceedBinding
import womenproject.com.mybury.presentation.main.MainFragmentDirections


/**
 * Created by HanAYeon on 2018. 11. 27..
 */

open class MainBucketListAdapter(context: Context?, bucketList: List<BucketItem>) : BaseBucketListAdapter(context, bucketList) {

    override fun getItemViewType(position: Int): Int {
        setLast(position)
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
        return if (bucketItemList[position].userCount == bucketItemList[position].goalCount) {
            0
        } else {
            bucketItemList[position].goalCount
        }
    }

    private fun setLast(position: Int) {
        bucketItemList[position].isLast = position == bucketItemList.size -1
    }

    override fun createOnClickBucketListener(bucket: BucketItem): View.OnClickListener {

        return View.OnClickListener {
            Log.e("ayhan", "goTo : ${bucket.title}")
            val directions = MainFragmentDirections.actionMainBucketToBucketDetail()
            directions.bucketId = bucket.id
            it.findNavController().navigate(directions)

        }
    }


}
