package womenproject.com.mybury.presentation.main.search

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.CategoryInfo
import womenproject.com.mybury.databinding.ItemSearchCategoryBinding

class SearchCategoryListViewHolder(private val binding: ItemSearchCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(categoryInfo: CategoryInfo) {
        binding.apply {
            this.category = categoryInfo
            itemOnClickListener = itemOnClickListener(categoryInfo)
            executePendingBindings()
        }
    }

    private fun itemOnClickListener(categoryInfo: CategoryInfo): View.OnClickListener {
        return View.OnClickListener {
            val directions = SearchFragmentDirections.actionSearchBucketToCategoryBucket()
            directions.category = categoryInfo
            it.findNavController().navigate(directions)
        }
    }
}