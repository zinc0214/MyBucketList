package womenproject.com.mybury.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.BucketListRepository

/**
 * Created by HanAYeon on 2018. 11. 28..
 */
class MainFragmentViewModel : BaseViewModel() {

    val items: LiveData<List<String>> =
            MutableLiveData<List<String>>().apply { value = BucketListRepository().getItemsPage() }


    fun getMainBucketList(): BucketList {

        /*
        * BucketType
        *   1 : 횟수1번
        *   2 : 횟수n번
        * DDay
        *   0 : dday 표기 안함
        *   1 : dday 표기함
        *   */

        val bucketItem1 = BucketItem("퇴사하고 한달동안 보드게임하기.", 1, 1, 0)
        val bucketItem2 = BucketItem("신전떡볶이 오뎅쿠폰 100장 모으기.", 15, 2, 0)
        val bucketItem3 = BucketItem("올림픽 공원에서 스케이트 타기", 1, 1, 1)
        val bucketItem4 = BucketItem("올림픽 공원에서 스케이트 타기", 95, 2, 1)



        val bucketItemList = ArrayList<BucketItem>()

        bucketItemList.add(bucketItem1)
        bucketItemList.add(bucketItem2)
        bucketItemList.add(bucketItem3)
        bucketItemList.add(bucketItem4)


        val bucketList = BucketList(bucketItemList)

        return bucketList
    }
}