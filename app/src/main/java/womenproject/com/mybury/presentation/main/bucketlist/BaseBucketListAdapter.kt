package womenproject.com.mybury.presentation.main.bucketlist

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.presentation.main.MainFragmentDirections

/**
 * Created by HanAYeon on 2019. 1. 22..
 */
open class BaseBucketListAdapter(context: Context?, bucketList: List<BucketItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var context: Context = context!!
    var bucketItemList = bucketList
    lateinit var currentViewHolder: BaseBucketItemViewHolder


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return currentViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder = currentViewHolder
        holder.apply {
            bind(createOnClickBucketListener(bucketItemList[position]), bucketItemList[position], context)
        }
    }

    open fun createOnClickBucketListener(bucket: BucketItem): View.OnClickListener {
        return View.OnClickListener {
            Log.e("ayhan", "goTo : ${bucket.title}")
            val directions = MainFragmentDirections.actionMainBucketToBucketDetail()
            directions.bucketId = bucket.id
            it.findNavController().navigate(directions)
        }
    }

    override fun getItemCount(): Int {
        return bucketItemList.size
    }


}
