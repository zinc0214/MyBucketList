package womenproject.com.mybury.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.databinding.BucketItemBaseBinding
import womenproject.com.mybury.databinding.BucketItemCountBinding
import womenproject.com.mybury.view.MainFragmentDirections
import womenproject.com.mybury.viewholder.BaseBucketItemViewHolder
import womenproject.com.mybury.viewholder.CountBucketItemViewHolder
import womenproject.com.mybury.viewholder.NoneCountBucketItemViewHolder


/**
 * Created by HanAYeon on 2018. 11. 27..
 */


// DiffUtil은 support library 24.2.0에서 추가된 클래스이다. 기존에 불편했던 RecyclerView의 효율적인 갱신 처리를 편리하게 다룰 수 있도록 제공하는 util 클래스이다.
class MainBucketListAdapter(context: Context?, bucketList: BucketList) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var context: Context = context!!
    private var bucketItemList = bucketList
    private lateinit var currentViewHolder: BaseBucketItemViewHolder


    override fun getItemViewType(position: Int): Int {
        return checkBucketType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        Log.e("ayhan:ViewType", "$viewType")

        currentViewHolder = CountBucketItemViewHolder(BucketItemCountBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
        return currentViewHolder as CountBucketItemViewHolder

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bucketList = position

        val type = checkBucketType(position)

        if (type == 1) {
            val holder = currentViewHolder
            holder.apply {
                bind(createOnClickBucketListener(bucketList.toString()), bucketItemList.list[position], context)
            }
        } else {
            val holder = currentViewHolder
            holder.apply {
                bind(createOnClickBucketListener(bucketList.toString()), bucketItemList.list[position], context)
            }
        }
    }


    private fun createOnClickBucketListener(bucketId: String): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(context, "count : $bucketId", Toast.LENGTH_SHORT).show()

            val directions = MainFragmentDirections.ActionMainBucketToBucketDetail(bucketId)
            it.findNavController().navigate(directions)
        }
    }


    override fun getItemCount(): Int {
        return bucketItemList.list.size
    }

    private fun checkBucketType(position: Int): Int {
        return bucketItemList.list[position].bucketType
    }
}


// TODO : ListView 의 마지막을 어떻게 다른 레이아웃으로 표시할 것인지 / 다이얼로그는 어떻게 할 것인지.