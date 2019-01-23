package womenproject.com.mybury.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.databinding.BucketItemBaseBinding
import womenproject.com.mybury.databinding.BucketItemCountBinding
import womenproject.com.mybury.databinding.BucketItemSucceedBinding
import womenproject.com.mybury.view.MainFragmentDirections
import womenproject.com.mybury.viewholder.BaseBucketItemViewHolder
import womenproject.com.mybury.viewholder.CountBucketItemViewHolder
import womenproject.com.mybury.viewholder.NoneCountBucketItemViewHolder
import womenproject.com.mybury.viewholder.SucceedBucketItemViewHolder

/**
 * Created by HanAYeon on 2019. 1. 22..
 */
open class BaseBucketListAdapter(context: Context?, bucketList: BucketList) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var context: Context = context!!
    var bucketItemList = bucketList
    lateinit var currentViewHolder: BaseBucketItemViewHolder


    override fun getItemViewType(position: Int): Int {
        return checkBucketType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder = currentViewHolder
        holder.apply {
            bind(createOnClickBucketListener(position), bucketItemList.list[position], context)
        }
    }


    open fun createOnClickBucketListener(bucketId: Int): View.OnClickListener {
        return View.OnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return bucketItemList.list.size
    }

    private fun checkBucketType(position: Int): Int {
        return bucketItemList.list[position].count
    }
}
