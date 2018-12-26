package womenproject.com.mybury.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.R

class SliderItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    val tvItem: TextView? = itemView?.findViewById(R.id.tv_item)
}