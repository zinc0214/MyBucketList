package womenproject.com.mybury.presentation.main.bucketlist

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.databinding.ItemBlankBinding
import womenproject.com.mybury.ui.loadingbutton.updateHeight

class BlankViewHolder(
    private val binding: ItemBlankBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ClickableViewAccessibility")
    fun bind(height: Int) {
        binding.apply {
            contentLayout.updateHeight(height)
            executePendingBindings()
        }
    }
}