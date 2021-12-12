package womenproject.com.mybury.ui

import androidx.recyclerview.widget.RecyclerView

interface ItemDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}

interface ItemCheckedListener {
    fun checked(isChecked: Boolean, item : Any)
}

interface ItemMovedListener {
    fun moved(list : List<Any>)
}
