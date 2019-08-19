package womenproject.com.mybury.presentation.mypage

import androidx.recyclerview.widget.RecyclerView
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