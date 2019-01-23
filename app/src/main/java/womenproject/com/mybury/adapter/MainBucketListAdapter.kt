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
import womenproject.com.mybury.databinding.BucketItemSucceedBinding
import womenproject.com.mybury.view.MainFragmentDirections
import womenproject.com.mybury.viewholder.BaseBucketItemViewHolder
import womenproject.com.mybury.viewholder.CountBucketItemViewHolder
import womenproject.com.mybury.viewholder.NoneCountBucketItemViewHolder
import womenproject.com.mybury.viewholder.SucceedBucketItemViewHolder


/**
 * Created by HanAYeon on 2018. 11. 27..
 */


// DiffUtil은 support library 24.2.0에서 추가된 클래스이다. 기존에 불편했던 RecyclerView의 효율적인 갱신 처리를 편리하게 다룰 수 있도록 제공하는 util 클래스이다.
class MainBucketListAdapter(context: Context?, bucketList: BucketList) : BaseBucketListAdapter(context, bucketList) {


    override fun getItemViewType(position: Int): Int {
        return checkBucketType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        Log.e("ayhan:ViewType", "$viewType")

        if (viewType == 1) {
            currentViewHolder = NoneCountBucketItemViewHolder(false, BucketItemBaseBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            return currentViewHolder as NoneCountBucketItemViewHolder
        } else if(viewType > 1){
            currentViewHolder = CountBucketItemViewHolder(false, BucketItemCountBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            return currentViewHolder as CountBucketItemViewHolder
        } else {
            currentViewHolder = SucceedBucketItemViewHolder(BucketItemSucceedBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            return currentViewHolder as SucceedBucketItemViewHolder
        }

    }

    private fun checkBucketType(position: Int): Int {
        return bucketItemList.list[position].count
    }

    override fun createOnClickBucketListener(bucketId: Int): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(context, "count : $bucketId", Toast.LENGTH_SHORT).show()

            val directions = MainFragmentDirections.ActionMainBucketToBucketDetail(bucketId.toString())
            it.findNavController().navigate(directions)
        }
    }

}
