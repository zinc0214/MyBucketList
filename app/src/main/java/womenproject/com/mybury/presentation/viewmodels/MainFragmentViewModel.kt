package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.CategoryList
import womenproject.com.mybury.data.network.bucketListApi

/**
 * Created by HanAYeon on 2018. 11. 28..
 */
class MainFragmentViewModel : BaseViewModel() {
//ㅇㅇㄴㅇㅇ


    interface OnBucketListGetEvent {
        fun start()
        fun finish(bucketList: BucketList?)
    }


    private var bucketList: BucketList? = null


    @SuppressLint("CheckResult")
    fun getMainBucketList(api: String, callback: OnBucketListGetEvent) {

        callback.start()

        bucketListApi.requestMainBucketListResult()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { bucketCategory -> callback.finish(bucketCategory)}
    }


    fun getDummyMainBucketList(): BucketList {


        val ca = CategoryList("운동")
        val list = mutableListOf<CategoryList>(ca)
        val category = BucketCategory(list,"20")


        val bucketItem1 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", false, false, false, ca, 4, 5)
        val bucketItem2 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", false, false, false, ca, 4, 5)
        val bucketItem3 = BucketItem("bucketlist02", "김가은,한아연,조지원,안예지 다들 너무 이쁘고 착하고 내가 진짜 너무 좋아해! 내 마음...완전 당빠 알자주지 흐듀휴류ㅠ휴ㅠㅠ휼유", false, false, false, ca, 4, 5)
        val bucketItem4 = BucketItem("bucketlist02", "김가은,한아연,조지원,안예지 다들 너무 이쁘고 착하고 내가 진짜 너무 좋아해! 내 마음...완전 당빠 알자주지 흐듀휴류ㅠ휴ㅠㅠ휼유", false, false, true, ca, 4, 5)
        val bucketItem5 = BucketItem("bucketlist02", "김가은,한아연,조지원,안예지 다들 너무", false, false, true, ca, 1, 1)
        val bucketItem6 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", false, false, false, ca, 1, 1)


        val bucketItemList = mutableListOf<BucketItem>()

        bucketItemList.add(bucketItem1)
        bucketItemList.add(bucketItem2)
        bucketItemList.add(bucketItem3)
        bucketItemList.add(bucketItem4)
        bucketItemList.add(bucketItem5)
        bucketItemList.add(bucketItem6)

        val bucketList = BucketList(bucketItemList, false, 200)

        return bucketList
    }
}