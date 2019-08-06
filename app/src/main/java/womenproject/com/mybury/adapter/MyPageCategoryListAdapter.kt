package womenproject.com.mybury.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.CategoryList
import womenproject.com.mybury.databinding.MypageCategoryItemBinding
import womenproject.com.mybury.viewholder.MyPageCategoryListViewHolder

/**
 * Created by HanAYeon on 2019. 1. 22..
 */
class MyPageCategoryListAdapter(context: Context?, bucketCategoryList: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var myPageCategoryListViewHolder: MyPageCategoryListViewHolder
    private val bucketCategory = bucketCategoryList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        myPageCategoryListViewHolder = MyPageCategoryListViewHolder(MypageCategoryItemBinding.inflate(android.view.LayoutInflater.from(parent.context), parent, false))
        return myPageCategoryListViewHolder
    }

    override fun getItemCount(): Int {
        return bucketCategory.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        myPageCategoryListViewHolder.bind(bucketCategory[position])
    }


}