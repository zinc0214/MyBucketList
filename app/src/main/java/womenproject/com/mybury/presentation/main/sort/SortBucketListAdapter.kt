package womenproject.com.mybury.presentation.main.sort

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.ItemBlankBinding
import womenproject.com.mybury.databinding.ItemSortBaseBucketBinding
import womenproject.com.mybury.databinding.ItemSortCountBucketBinding
import womenproject.com.mybury.databinding.ItemSortDoneBucketBinding
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.ui.ItemTouchHelperListener2
import womenproject.com.mybury.util.Converter.Companion.dpToPx

class SortBucketListAdapter(
    private val bucketList: MutableList<BucketItem> = mutableListOf(),
    private val dragListener: ItemDragListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperListener2 {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType.toType()) {
            ItemType.BASE -> {
                SortBaseBucketViewHolder(
                    ItemSortBaseBucketBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    dragListener
                )
            }

            ItemType.COUNT -> {
                SortCountBucketViewHolder(
                    ItemSortCountBucketBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    dragListener
                )
            }

            ItemType.DONE -> {
                SortDoneBucketViewHolder(
                    ItemSortDoneBucketBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    dragListener
                )
            }

            else -> {
                BlankViewHolder(
                    ItemBlankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == bucketList.size) {
            return 3
        }
        val currentItemInfo = bucketList[position]

        val type = when {
            currentItemInfo.goalCount == currentItemInfo.userCount -> {
                ItemType.DONE
            }

            currentItemInfo.goalCount > 1 -> {
                ItemType.COUNT
            }

            else -> {
                ItemType.BASE
            }
        }

        return type.toInt()
    }

    override fun getItemCount(): Int {
        return bucketList.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SortBaseBucketViewHolder -> {
                holder.bind(bucketList[position])
            }

            is SortCountBucketViewHolder -> {
                holder.bind(bucketList[position])
            }

            is SortDoneBucketViewHolder -> {
                holder.bind(bucketList[position])
            }

            is BlankViewHolder -> {
                holder.bind(dpToPx(60))
            }
        }
    }

//    override fun onItemMoved(from: Int, to: Int) {
//        val toReal = if (to == 0) 0 else to - 1
//        if (from == toReal) {
//            return
//        }
//
//        val bucketList = bucketList as MutableList<BucketItem>
//        val fromItem = bucketList.removeAt(from)
//        bucketList.add(toReal, fromItem)
//        notifyItemMoved(from, toReal)
//
//        itemMovedListener.moved(bucketList)
//    }

    enum class ItemType {
        BLANK, BASE, COUNT, DONE;

        fun toInt(): Int {
            return when {
                this == BASE -> 0
                this == COUNT -> 1
                this == DONE -> 2
                else -> 3
            }
        }
    }

    private fun Int.toType(): ItemType {
        return when {
            this == 0 -> ItemType.BASE
            this == 1 -> ItemType.COUNT
            this == 2 -> ItemType.DONE
            else -> ItemType.BLANK
        }
    }

    override fun onItemMove(from_position: Int, to_position: Int): Boolean {
        if(to_position > bucketList.lastIndex) return false
        val item = bucketList[from_position]
        // 리스트 갱신
        bucketList.removeAt(from_position)
        bucketList.add(to_position, item)

        notifyItemMoved(from_position, to_position)
        return true
    }

    fun updateBucketList(updateList: List<BucketItem>) {
        bucketList.clear()
        bucketList.addAll(updateList)
    }

    fun getBucketList() = bucketList
}