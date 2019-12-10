package womenproject.com.mybury.presentation.viewmodels

import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.Category

/**
 * Created by HanAYeon on 2018. 11. 28..
 */
class MainFragmentViewModel : BaseViewModel() {


    fun getDummyMainBucketList(): List<BucketItem> {

        val ca = Category("없음", "191919199119",0)
        val ca2 = Category("여행", "2020202020", 1)
        val list = mutableListOf<Category>(ca)
        val category = BucketCategory(list,"20")


        val bucketItem1 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", "메모메모", false, false,  ca, 2, 5, 0,false)
       /* val bucketItem2 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", "", false, false, false, ca2, 3, 10, 10)
        val bucketItem3 = BucketItem("bucketlist02", "김가은,한아연,조지원,안예지 다들 너무 이쁘고 착하고 내가 진짜 너무 좋아해! 내 마음...완전 당빠 알자주지 흐듀휴류ㅠ휴ㅠㅠ휼유", "메모메모", false, false, false, ca2, 4, 5, 3)
        val bucketItem4 = BucketItem("bucketlist02", "김가은,한아연,조지원,안예지 다들 너무 이쁘고 착하고 내가 진짜 너무 좋아해! 내 마음...완전 당빠 알자주지 흐듀휴류ㅠ휴ㅠㅠ휼유", "", false, false, true, ca, 9, 15, 10)
        val bucketItem5 = BucketItem("bucketlist02", "김가은,한아연,조지원,안예지 다들 너무", "", false, false, true, ca2, 1, 1, 0)
        val bucketItem6 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", "", false, false, false, ca2, 1, 1, 1)

*/
        val bucketItemList = mutableListOf<BucketItem>()

        bucketItemList.add(bucketItem1)
      /*  bucketItemList.add(bucketItem2)
        bucketItemList.add(bucketItem3)
        bucketItemList.add(bucketItem4)
        bucketItemList.add(bucketItem5)
        bucketItemList.add(bucketItem6)*/

        val bucketList = BucketList(bucketItemList, false, 200)

        return bucketList.bucketlists
    }
}