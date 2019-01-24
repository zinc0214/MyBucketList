package womenproject.com.mybury.viewmodels

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

        val bucketItem1 = BucketItem("퇴사하고 한달동안 보드게임하기.", 1,  0)
        val bucketItem2 = BucketItem("신전떡볶이 오뎅쿠폰 10장 모으기.", 9,  5, true)
        val bucketItem3 = BucketItem("올림픽 공원에서 스케이트 타기", 1,  1)
        val bucketItem4 = BucketItem("올림픽 공원에서 스케이트 타기", 5,  4, true)
        val bucketItem5 = BucketItem("짜장면 10번 먹기", 8, 2)
        val bucketItem6 = BucketItem("올림픽 공원에서 스케이트 타기2", 1, 4, true)
        val bucketItem7 = BucketItem("영화 10번 보기", 6, 2)
        val bucketItem8 = BucketItem("포트폴리오 만들기", 0, 1)
        val bucketItem9 = BucketItem("김가은,한아연,조지원,안예지 다들 너무 이쁘고 착하고 내가 진짜 너무 좋아해! 그렇대요!!", 1, 1)



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


        val bucketList = BucketList(bucketItemList)

        return bucketList
    }
}