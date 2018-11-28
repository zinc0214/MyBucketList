package womenproject.com.mybury.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.databinding.ListItemBucketMainBinding
import womenproject.com.mybury.util.SwipeLayout

/**
 * Created by HanAYeon on 2018. 11. 27..
 */


// DiffUtil은 앞에서도 말했다시피 support library 24.2.0에서 추가된 클래스이다. 기존에 불편했던 RecyclerView의 효율적인 갱신 처리를 편리하게 다룰 수 있도록 제공하는 util 클래스이다.
class MainBucketListAdapter : RecyclerView.Adapter<MainBucketListAdapter.ViewHolder>() {

    private lateinit var swipeLayout: SwipeLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainBucketListAdapter.ViewHolder {
        return ViewHolder(ListItemBucketMainBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MainBucketListAdapter.ViewHolder, position: Int) {
        val bucketList = getItemId(position)

        //binding.sample1.addRevealListener(R.id.delete) { child: View, edge: SwipeLayout.DragEdge, fraction: Float, distance: Int -> }

        holder.apply {

            holder.apply {
                bind(createOnClickListener(bucketList.toString()))

            }
        }
    }

    override fun getItemCount(): Int {
        return 10
    }

    private fun createOnClickListener(plantId: String): View.OnClickListener {
        return View.OnClickListener {

        }
    }

    class ViewHolder(private val binding: ListItemBucketMainBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener) {
            binding.apply {
                clickListener = listener
                executePendingBindings()

                binding.sample1.addRevealListener(R.id.delete) { child: View, edge: SwipeLayout.DragEdge, fraction: Float, distance: Int -> }

                binding.sample1.showMode = SwipeLayout.ShowMode.PullOut
                binding.sample1.addDrag(SwipeLayout.DragEdge.Left, binding.sample1.findViewById<LinearLayout>(R.id.bottom_wrapper))
                binding.sample1.addDrag(SwipeLayout.DragEdge.Right, binding.sample1.findViewById<LinearLayout>(R.id.bottom_wrapper_2))

            }

        }


    }
}


/*: RecyclerView.Adapter<MainBucketListAdapter.ViewHolder>() {

    private lateinit var swipeLayout: SwipeLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_bucket_main, parent, false)
        return ViewHolder(layoutView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        swipeLayout.addRevealListener(R.id.delete) { child: View, edge: SwipeLayout.DragEdge, fraction: Float, distance: Int -> }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        init {
            swipeLayout = mView.findViewById(R.id.sample1)

            swipeLayout.showMode = SwipeLayout.ShowMode.PullOut

            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById<LinearLayout>(R.id.bottom_wrapper))
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, swipeLayout.findViewById<LinearLayout>(R.id.bottom_wrapper_2))
        }
    }

    override fun getItemCount(): Int {
        return 10
    }*/
