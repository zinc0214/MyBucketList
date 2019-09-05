package womenproject.com.mybury.presentation.main.bucketlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.databinding.BucketItemBaseBinding
import womenproject.com.mybury.databinding.BucketItemCountBinding
import womenproject.com.mybury.databinding.BucketItemSucceedBinding
import womenproject.com.mybury.presentation.base.BaseBucketListAdapter
import womenproject.com.mybury.presentation.main.MainFragmentDirections
import womenproject.com.mybury.presentation.base.BaseCountBucketItemViewHolder


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

    override fun createOnClickBucketListener(bucket: BucketItem): View.OnClickListener {

        return View.OnClickListener {
            Toast.makeText(context, "count : ${bucket.title}", Toast.LENGTH_SHORT).show()
            val directions = MainFragmentDirections.actionMainBucketToBucketDetail()
            directions.bucket = bucket
            it.findNavController().navigate(directions)

        }
    }
}
