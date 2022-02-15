package womenproject.com.mybury.presentation.main.search

import android.view.View
import android.widget.LinearLayout
import androidx.core.view.setMargins
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.CategoryInfo
import womenproject.com.mybury.databinding.ItemSearchCategoryBinding
import womenproject.com.mybury.util.Converter

class SearchCategoryListViewHolder(private val binding: ItemSearchCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(categoryInfo: CategoryInfo, isLastItem: Boolean) {
        binding.apply {
            this.category = categoryInfo
            itemOnClickListener = itemOnClickListener(categoryInfo)
            if(isLastItem){
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                lp.setMargins(Converter.dpToPx(8))
                contentLayout.layoutParams = lp
            }
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