package womenproject.com.mybury.viewmodels

import android.util.Log
import womenproject.com.mybury.data.*

/**
 * Created by HanAYeon on 2019. 1. 16..
 */

class DdayBucketTotalListViewModel : BaseViewModel() {

    fun getDdayEachBucketList(): DdayTotalBucketList {

        var ddayBucketGroupList = ArrayList<DdayEachBucketGroup>()

        getDdayEachBucketItem().groupBy { it.d_day }.forEach {
            val bucketGroup = DdayEachBucketGroup(it.key, it.value)
            ddayBucketGroupList.add(bucketGroup)
        }


        val ddaySortedByBucketGroupList = ddayBucketGroupList.sortedBy { it.dday }
        val ddayBucketTotalList = DdayTotalBucketList(ddaySortedByBucketGroupList)

        for (i in 0 until ddaySortedByBucketGroupList.size) {
            Log.e("ayhan", "${ddaySortedByBucketGroupList[i]}")
        }


        return ddayBucketTotalList
    }


    private fun getDdayEachBucketItem(): ArrayList<BucketItem> {
        val category = BucketCategory("id", "운동")
        val bucketItem1 = BucketItem("bucketlist02", "퇴사하고 한달동안 보드게임하기.", false, false, false, category, 2, 5)
        val bucketItem2 = BucketItem("bucketlist02", "신전떡볶이 오뎅쿠폰 10장 모으기.", false, false, false ,category, 0, 1)
        val bucketItem3 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", false, false, true, category, 2, 5)


        val bucketItemList = ArrayList<BucketItem>()

        bucketItemList.add(bucketItem1)
        bucketItemList.add(bucketItem2)
        bucketItemList.add(bucketItem3)

        return bucketItemList
    }
}