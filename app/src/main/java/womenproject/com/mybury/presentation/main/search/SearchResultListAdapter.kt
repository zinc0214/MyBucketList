package womenproject.com.mybury.presentation.main.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.CategoryInfo
import womenproject.com.mybury.data.SearchResultType
import womenproject.com.mybury.data.SearchType
import womenproject.com.mybury.databinding.ItemBucketDoingSimpleBinding
import womenproject.com.mybury.databinding.ItemMypageCategoryBinding
import womenproject.com.mybury.presentation.main.bucketlist.BaseBucketItemViewHolder
import womenproject.com.mybury.presentation.mypage.categoryedit.MyPageCategoryListViewHolder


/**
 * Created by HanAYeon on 2018. 11. 27..
 */

class SearchResultListAdapter(
    private val showSnackBar: (BucketItem) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    private val resultList = arrayListOf<SearchResultType>()
    private var searchType = SearchType.All

    override fun getItemViewType(position: Int): Int {
        return searchType.toInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            // all, dday
            0, 1 -> BaseBucketItemViewHolder(
                ItemBucketDoingSimpleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), showSnackBar
            )
            // category
            else -> MyPageCategoryListViewHolder(
                ItemMypageCategoryBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is BaseBucketItemViewHolder -> {
                val bucketItem = resultList[position] as BucketItem
                if (searchType == SearchType.All) {
                    holder.bind(
                        createOnClickBucketListener(bucketItem),
                        bucketItem,
                        false
                    )
                } else {
                    holder.bind(
                        createOnClickBucketListener(bucketItem),
                        bucketItem,
                        isForDday = true,
                        isShowDday = true
                    )
                }

            }
            is MyPageCategoryListViewHolder -> {
                val category = resultList[position] as CategoryInfo
                holder.bind(category)
            }
        }
    }

    private fun createOnClickBucketListener(bucket: BucketItem) = View.OnClickListener {
        val directions = SearchFragmentDirections.actionSearchBucketToBucketDetail()
        directions.bucketId = bucket.id
        it.findNavController().navigate(directions)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(searchType: SearchType, list: List<SearchResultType>) {
        resultList.clear()
        resultList.addAll(list)
        this.searchType = searchType
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteList() {
        resultList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return resultList.size
    }
}
