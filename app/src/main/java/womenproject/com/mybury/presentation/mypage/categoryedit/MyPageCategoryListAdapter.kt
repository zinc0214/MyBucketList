package womenproject.com.mybury.presentation.mypage.categoryedit

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.MyPageCategory
import womenproject.com.mybury.databinding.ItemMypageCategoryBinding

/**
 * Created by HanAYeon on 2019. 1. 22..
 */
class MyPageCategoryListAdapter(context: Context?, bucketCategoryList: MutableList<MyPageCategory>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var myPageCategoryListViewHolder: MyPageCategoryListViewHolder
    private val bucketCategory = bucketCategoryList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        myPageCategoryListViewHolder = MyPageCategoryListViewHolder(ItemMypageCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return myPageCategoryListViewHolder
    }

    override fun getItemCount(): Int {
        return bucketCategory.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        myPageCategoryListViewHolder.bind(bucketCategory[position])
    }


}