package womenproject.com.mybury.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.BucketCategoryList
import womenproject.com.mybury.databinding.CategoryListItemBinding
import womenproject.com.mybury.ui.ItemActionListener
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.viewholder.EditCategoryListViewHolder

class EditCategoryListAdapter(private val bucketCategoryList: BucketCategoryList,
                              private val listener: ItemDragListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemActionListener {


    private lateinit var editCategoryListViewHolder: EditCategoryListViewHolder
    private val categoryList : MutableList<BucketCategory> = bucketCategoryList.categoryList as MutableList<BucketCategory>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        editCategoryListViewHolder = EditCategoryListViewHolder(CategoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener)
        return editCategoryListViewHolder
    }

    override fun getItemCount(): Int {
        return bucketCategoryList.categoryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        editCategoryListViewHolder.bind(bucketCategoryList.categoryList[position])
    }

    override fun onItemMoved(from: Int, to: Int) {
        if (from == to) {
            return
        }

        val fromItem = categoryList.removeAt(from)
        categoryList.add(to, fromItem)
        notifyItemMoved(from, to)
    }

}