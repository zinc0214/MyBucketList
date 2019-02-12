package womenproject.com.mybury.viewmodels

import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList

/**
 * Created by HanAYeon on 2018. 11. 28..
 */
class MainFragmentViewModel : BaseViewModel() {

    fun getMainBucketList(): BucketList {

        /*
        * BucketType
        *   1 : 횟수1번
        *   2 : 횟수n번
        *   3 : 완료
        *   4 : 빈 버킷
        * DDay
        *   0 : dday 표기 안함
        *   1 : dday 표기함
        *   */

        val category = BucketCategory("id", "운동")
        val bucketItem1 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", false, false, category, 2, 5)
        val bucketItem2 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", false, false, category, 2, 5)
        val bucketItem3 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", false, false, category, 2, 5)

        val bucketItemList = ArrayList<BucketItem>()

        bucketItemList.add(bucketItem1)
        bucketItemList.add(bucketItem2)
        bucketItemList.add(bucketItem3)

        val bucketList = BucketList(bucketItemList)

        return bucketList
    }
}