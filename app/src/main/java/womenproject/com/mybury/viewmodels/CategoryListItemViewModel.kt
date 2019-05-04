package womenproject.com.mybury.viewmodels

import womenproject.com.mybury.base.BaseViewModel
import womenproject.com.mybury.data.BucketCategory

class CategoryListItemViewModel(bucketCategory: BucketCategory) : BaseViewModel() {

    val bucketId = bucketCategory.id
    val bucketName = bucketCategory.name
}