package womenproject.com.mybury.viewmodels

import womenproject.com.mybury.base.BaseViewModel
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.CategoryList

class CategoryListItemViewModel(categoryName : String) : BaseViewModel() {

    val bucketName = categoryName
}