package womenproject.com.mybury.ui

import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.Category

interface ItemDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}

interface ItemCheckedListener {
    fun checked(isChecked: Boolean, item : Category)
}

interface ItemMovedListener {
    fun moved(list : List<Category>)
}
