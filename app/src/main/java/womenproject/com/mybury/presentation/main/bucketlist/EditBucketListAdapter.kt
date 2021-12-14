package womenproject.com.mybury.presentation.main.bucketlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.ItemBlankBinding
import womenproject.com.mybury.databinding.ItemEditBucketBinding
import womenproject.com.mybury.ui.ItemActionListener
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.ui.ItemMovedListener
import womenproject.com.mybury.util.Converter.Companion.dpToPx

class EditBucketListAdapter(
    private val bucketList: List<BucketItem>,
    private val dragListener: ItemDragListener,
    private val itemMovedListener: ItemMovedListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemActionListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == 0) {
            EditBucketListViewHolder(
                ItemEditBucketBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                dragListener
            )
        } else {
            BlankViewHolder(
                ItemBlankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == bucketList.size) {
            1
        } else {
            0
        }
    }

    override fun getItemCount(): Int {
        return bucketList.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EditBucketListViewHolder -> {
                holder.bind(bucketList[position])
            }
            is BlankViewHolder -> {
                holder.bind(dpToPx(60))
            }
        }
    }

    override fun onItemMoved(from: Int, to: Int) {
        val toReal = if (to == 0) 0 else to - 1
        if (from == toReal) {
            return
        }

        val bucketList = bucketList as MutableList<BucketItem>
        val fromItem = bucketList.removeAt(from)
        bucketList.add(toReal, fromItem)
        notifyItemMoved(from, toReal)

        itemMovedListener.moved(bucketList)
    }

}