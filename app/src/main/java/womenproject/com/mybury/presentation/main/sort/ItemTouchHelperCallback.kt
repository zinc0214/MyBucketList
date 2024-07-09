package womenproject.com.mybury.presentation.main.sort

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

interface BucketItemTouchHelperListener {
    fun onItemMove(from_position: Int, to_position: Int): Boolean
}

class ItemTouchHelperCallback(val listener: BucketItemTouchHelperListener) :
    ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        // 드래그 방향
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        // 이동을 만드는 메소드
        return makeMovementFlags(dragFlags, 0)
    }

    // 드래그된 item을 이전 위치에서 새로운 위치로 옮길 때 호출
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // 리스너의 onMove 메소드 호출
        return listener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }
}