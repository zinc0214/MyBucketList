package womenproject.com.mybury.presentation.mypage.categoryedit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.databinding.CategoryListItemBinding
import womenproject.com.mybury.ui.ItemActionListener
import womenproject.com.mybury.ui.ItemCheckedListener
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.ui.ItemMovedListener

class EditCategoryListAdapter(private val bucketCategoryList: List<Category>,
                              private val dragListener: ItemDragListener,
                              private val checkedListener: ItemCheckedListener,
                              private val itemMovedListener: ItemMovedListener,
                              private val editCategoryName: (Category) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemActionListener {


    private lateinit var editCategoryListViewHolder: EditCategoryListViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        editCategoryListViewHolder = EditCategoryListViewHolder(CategoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                dragListener, checkedListener, editCategoryName)
        return editCategoryListViewHolder
    }

    override fun getItemCount(): Int {
        return bucketCategoryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        editCategoryListViewHolder.bind(bucketCategoryList[position])
    }

    override fun onItemMoved(from: Int, to: Int) {
        if (from == to) {
            return
        }

        val categoryList = bucketCategoryList as MutableList<Category>
        val fromItem = categoryList.removeAt(from)
        categoryList.add(to, fromItem)
        notifyItemMoved(from, to)

        itemMovedListener.moved(categoryList)
    }

}