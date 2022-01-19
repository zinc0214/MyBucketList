package womenproject.com.mybury.presentation.mypage.categoryedit

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.CategoryInfo
import womenproject.com.mybury.databinding.ItemMypageCategoryBinding
import womenproject.com.mybury.presentation.mypage.MyPageFragmentDirections

/**
 * Created by HanAYeon on 2019. 4. 30..
 */

class MyPageCategoryListViewHolder(private val binding: ItemMypageCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(categoryInfo: CategoryInfo) {
        binding.apply {
            categoryName = categoryInfo.name
            categoryCount = categoryInfo.count.toString()
            itemOnClickListener = itemOnClickListener(categoryInfo)
            executePendingBindings()
        }
    }

    private fun itemOnClickListener(categoryInfo: CategoryInfo): View.OnClickListener {
        return View.OnClickListener {
            val directions = MyPageFragmentDirections.actionMyPageToBucketItemByCategory()
            directions.category = categoryInfo
            it.findNavController().navigate(directions)
        }
    }
}