package womenproject.com.mybury.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.databinding.BucketItemBaseBinding

import womenproject.com.mybury.view.MainFragmentDirections

/**
 * Created by HanAYeon on 2018. 11. 27..
 */


// DiffUtil은 support library 24.2.0에서 추가된 클래스이다. 기존에 불편했던 RecyclerView의 효율적인 갱신 처리를 편리하게 다룰 수 있도록 제공하는 util 클래스이다.
class MainBucketListAdapter(context: Context?) : RecyclerView.Adapter<MainBucketListAdapter.ViewHolder>() {

    private var contextM: Context = context!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainBucketListAdapter.ViewHolder {
        return ViewHolder(BucketItemBaseBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MainBucketListAdapter.ViewHolder, position: Int) {
        val bucketList = position

        holder.apply {
            bind(createOnClickBucketListener(bucketList.toString()))
        }
    }

    private fun createOnClickBucketListener(bucketId: String): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(contextM, "count : $bucketId", Toast.LENGTH_SHORT).show()

            val directions = MainFragmentDirections.ActionMainBucketToBucketDetail(bucketId)
            it.findNavController().navigate(directions)
        }
    }


    class ViewHolder(private val binding: BucketItemBaseBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bucketListener: View.OnClickListener) {
            binding.apply {
                bucketClickListener = bucketListener
                executePendingBindings()
            }
        }
    }

    override fun getItemCount(): Int {
        return 10
    }
}


// TODO : ListView 의 마지막을 어떻게 다른 레이아웃으로 표시할 것인지 / 다이얼로그는 어떻게 할 것인지.