package womenproject.com.mybury.viewmodels

import android.util.Log
import womenproject.com.mybury.data.*

/**
 * Created by HanAYeon on 2019. 1. 16..
 */

class DdayBucketTotalListViewModel : BaseViewModel() {

    fun getDdayEachBucketList(): DdayTotalBucketList {

        var ddayBucketGroupList = ArrayList<DdayEachBucketGroup>()

        getDdayEachBucketItem().groupBy { it.dday }.forEach {
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
        val bucketItem1 = BucketItem("퇴사하고 한달동안 보드게임하기.", 1,  1)
        val bucketItem2 = BucketItem("신전떡볶이 오뎅쿠폰 10장 모으기.", 9,  5)
        val bucketItem3 = BucketItem("올림픽 공원에서 스케이트 타기", 0,  5)
        val bucketItem4 = BucketItem("가나라다", 1,  3)
        val bucketItem5 = BucketItem("마바사", 0,  3)
        val bucketItem6 = BucketItem("아자차카", 1,  3)
        val bucketItem7 = BucketItem("고래고래", 5,  4)
        val bucketItem8 = BucketItem("아이스크림", 2,  5)
        val bucketItem9 = BucketItem("둥가둥가", 7,  1)
        val bucketItem10 = BucketItem("뇸뇸뇸", 1,  7)


        val bucketItemList = ArrayList<BucketItem>()

        bucketItemList.add(bucketItem1)
        bucketItemList.add(bucketItem2)
        bucketItemList.add(bucketItem3)
        bucketItemList.add(bucketItem4)
        bucketItemList.add(bucketItem5)
        bucketItemList.add(bucketItem6)
        bucketItemList.add(bucketItem7)
        bucketItemList.add(bucketItem8)
        bucketItemList.add(bucketItem9)
        bucketItemList.add(bucketItem10)

        return bucketItemList
    }
}