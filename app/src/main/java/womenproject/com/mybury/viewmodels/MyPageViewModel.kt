package womenproject.com.mybury.viewmodels

import womenproject.com.mybury.base.BaseViewModel
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.BucketCategoryList

/**
 * Created by HanAYeon on 2019. 4. 23..
 */

class MyPageViewModel : BaseViewModel() {

    var nickname = "닉네임이 이렇게 길다리다링루룽"
    var doingBucketCount = "27"
    var doneBucketCount = "31"
    var ddayCount = "20"

    fun categoryList() : BucketCategoryList {

        val categoryItem1 = BucketCategory("1", "없음", 1)
        val categoryItem2 = BucketCategory("2", "음식", 10)
        val categoryItem3 = BucketCategory("3", "여행", 15)
        val categoryItem4 = BucketCategory("4", "언어공부", 5)
        val categoryItem5 = BucketCategory("5", "두룽두울부룽둥", 40)

        val list = ArrayList<BucketCategory>()

        list.add(categoryItem1)
        list.add(categoryItem2)
        list.add(categoryItem3)
        list.add(categoryItem4)
        list.add(categoryItem5)

        val bucketCategory = BucketCategoryList(list)

        return bucketCategory
    }

}