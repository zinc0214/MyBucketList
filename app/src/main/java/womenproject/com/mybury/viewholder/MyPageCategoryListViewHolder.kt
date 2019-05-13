package womenproject.com.mybury.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.databinding.MypageCategoryItemBinding

/**
 * Created by HanAYeon on 2019. 4. 30..
 */

class MyPageCategoryListViewHolder(private val binding: MypageCategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(name: String) {
        binding.apply {

            categoryName = name

            executePendingBindings()
        }
    }
}