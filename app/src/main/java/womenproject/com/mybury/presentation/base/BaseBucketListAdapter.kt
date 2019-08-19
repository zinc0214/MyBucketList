package womenproject.com.mybury.presentation.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketList

/**
 * Created by HanAYeon on 2019. 1. 22..
 */
open class BaseBucketListAdapter(context: Context?, bucketList: BucketList) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var context: Context = context!!
    var bucketItemList = bucketList
    lateinit var currentViewHolder: BaseBucketItemViewHolder


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder = currentViewHolder
        holder.apply {
            bind(createOnClickBucketListener(position), bucketItemList.bucketlists[position], context)
        }
    }

    open fun createOnClickBucketListener(bucketId: Int): View.OnClickListener {
        return View.OnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return bucketItemList.bucketlists.size
    }


}
