package womenproject.com.mybury.presentation.main.bucketlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.ItemEditBucketBinding
import womenproject.com.mybury.ui.ItemActionListener
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.ui.ItemMovedListener

class EditBucketListAdapter(
    private val bucketList: List<BucketItem>,
    private val dragListener: ItemDragListener,
    private val itemMovedListener: ItemMovedListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemActionListener {

    private lateinit var editBucketListViewHolder: EditBucketListViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        editBucketListViewHolder = EditBucketListViewHolder(
            ItemEditBucketBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            dragListener
        )

        return editBucketListViewHolder
    }

    override fun getItemCount(): Int {
        return bucketList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        editBucketListViewHolder.bind(bucketList[position])
    }

    override fun onItemMoved(from: Int, to: Int) {
        if (from == to) {
            return
        }

        val bucketList = bucketList as MutableList<BucketItem>
        val fromItem = bucketList.removeAt(from)
        bucketList.add(to, fromItem)
        notifyItemMoved(from, to)

        itemMovedListener.moved(bucketList)
    }

}