package womenproject.com.mybury.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.ListItemBucketMainBinding
import womenproject.com.mybury.util.SwipeLayout
import womenproject.com.mybury.view.MainFragmentDirections

/**
 * Created by HanAYeon on 2018. 11. 27..
 */


// DiffUtil은 support library 24.2.0에서 추가된 클래스이다. 기존에 불편했던 RecyclerView의 효율적인 갱신 처리를 편리하게 다룰 수 있도록 제공하는 util 클래스이다.
class MainBucketListAdapter(context: Context?) : RecyclerView.Adapter<MainBucketListAdapter.ViewHolder>() {

    private var contextM: Context = context!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainBucketListAdapter.ViewHolder {
        return ViewHolder(ListItemBucketMainBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MainBucketListAdapter.ViewHolder, position: Int) {
        val bucketList = position

        holder.apply {
            bind(createOnClickBucketListener(bucketList.toString()),createOnClickWriteListener())
        }
    }

    private fun createOnClickBucketListener(bucketId: String): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(contextM, "count : $bucketId", Toast.LENGTH_SHORT).show()

            val directions = MainFragmentDirections.ActionMainBucketToBucketDetail(bucketId)
            it.findNavController().navigate(directions)
        }
    }

    private fun createOnClickWriteListener() : View.OnClickListener {
        return View.OnClickListener {
            val directions = MainFragmentDirections.ActionMainBucketToBucketWrite()
            it.findNavController().navigate(directions)
        }
    }
    class ViewHolder(private val binding: ListItemBucketMainBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bucketListener: View.OnClickListener, writeListener: View.OnClickListener) {
            binding.apply {
                bucketClickListener = bucketListener
                writeClickListener = writeListener
                executePendingBindings()

                binding.sample1.addRevealListener(R.id.delete) { child: View, edge: SwipeLayout.DragEdge, fraction: Float, distance: Int -> }

                binding.sample1.showMode = SwipeLayout.ShowMode.PullOut
                binding.sample1.addDrag(SwipeLayout.DragEdge.Left, binding.sample1.findViewById<LinearLayout>(R.id.bottom_wrapper))
                binding.sample1.addDrag(SwipeLayout.DragEdge.Right, binding.sample1.findViewById<LinearLayout>(R.id.bottom_wrapper_2))

            }
        }
    }

    override fun getItemCount(): Int {
        return 10
    }
}


// TODO : ListView 의 마지막을 어떻게 다른 레이아웃으로 표시할 것인지 / 다이얼로그는 어떻게 할 것인지.