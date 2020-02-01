package womenproject.com.mybury.presentation.mypage.categoryedit

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.MyPageCategory
import womenproject.com.mybury.databinding.MypageCategoryItemBinding
import womenproject.com.mybury.presentation.mypage.MyPageFragmentDirections

/**
 * Created by HanAYeon on 2019. 4. 30..
 */

class MyPageCategoryListViewHolder(private val binding: MypageCategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(category: MyPageCategory) {
        binding.apply {
            categoryName = category.name
            categoryCount = category.count.toString()
            itemOnClickListener = itemOnClickListener(category)
            executePendingBindings()
        }
    }

    private fun itemOnClickListener(category: MyPageCategory): View.OnClickListener {
        return View.OnClickListener {
            val directions = MyPageFragmentDirections.actionMyPageToBucketItemByCategory()
            directions.category = category
            it.findNavController().navigate(directions)
        }
    }
}