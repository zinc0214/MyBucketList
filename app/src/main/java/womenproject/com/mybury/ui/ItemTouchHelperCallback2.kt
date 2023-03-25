package womenproject.com.mybury.ui

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

// RecyclerView의 Adapter와 ItemTouchHelper.Callback을 연결시켜 주는 리스너
interface ItemTouchHelperListener2 {
    fun onItemMove(from_position: Int, to_position: Int): Boolean
}

// ItemTouchHelper.Callback 클래스를 상속
class ItemTouchHelperCallback2(val listener: ItemTouchHelperListener2)
    : ItemTouchHelper.Callback() {
    // 활성화된 이동 방향을 정의하는 플래그를 반환하는 메소드
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        // 드래그 방향
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        // 스와이프 방향
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
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