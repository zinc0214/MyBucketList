package womenproject.com.mybury.viewmodels

import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import womenproject.com.mybury.base.BaseViewModel
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.network.OkHttp3RetrofitManager
import womenproject.com.mybury.network.RetrofitInterface

/**
 * Created by HanAYeon on 2018. 11. 28..
 */
class MainFragmentViewModel : BaseViewModel() {

    private var bucketList : BucketList? = null
    var progressVisible = ObservableInt(View.GONE)


    interface OnBucketListGetEvent {
        fun start()
        fun finish(bucketList: BucketList?)
    }

    fun getMainBucketList(api: String, callback: OnBucketListGetEvent): BucketList? {

        callback.start()

        val restClient: RetrofitInterface = OkHttp3RetrofitManager(api).getRetrofitService(RetrofitInterface::class.java)

        val bucketListResultData = restClient.requestMainBucketListResult()
        bucketListResultData.enqueue(object : Callback<BucketList> {
            override fun onResponse(call: Call<BucketList>?, response: Response<BucketList>?) {

                if (response != null && response.isSuccessful) {
                    Log.e("ayhan:result", "${response.body()}")
                    bucketList = response.body()
                    Log.e("ayhan:bucketList", "$bucketList")
                    callback.finish(bucketList)
                }
            }

            override fun onFailure(call: Call<BucketList>?, t: Throwable?) {
                Log.e("ayhan2", t.toString())
                //progressVisible.set(View.GONE)
                callback.finish(bucketList)
            }
        })

        return bucketList

    }


/*    fun getMainBucketList(): BucketList {

        *//*
        * BucketType
        *   1 : 횟수1번
        *   2 : 횟수n번
        *   3 : 완료
        *   4 : 빈 버킷
        * DDay
        *   0 : dDate 표기 안함
        *   1 : dDate 표기함
        *   *//*

        val category = BucketCategory("id", "운동", 20)


        val bucketItem1 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", false, false, false, category, 4, 5)
        val bucketItem2 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", false, false, false, category, 4, 5)
        val bucketItem3 = BucketItem("bucketlist02", "김가은,한아연,조지원,안예지 다들 너무 이쁘고 착하고 내가 진짜 너무 좋아해! 내 마음...완전 당빠 알자주지 흐듀휴류ㅠ휴ㅠㅠ휼유", false, false, false, category, 4, 5)
        val bucketItem4 = BucketItem("bucketlist02", "김가은,한아연,조지원,안예지 다들 너무 이쁘고 착하고 내가 진짜 너무 좋아해! 내 마음...완전 당빠 알자주지 흐듀휴류ㅠ휴ㅠㅠ휼유", false, false, true, category, 4, 5)
        val bucketItem5 = BucketItem("bucketlist02", "김가은,한아연,조지원,안예지 다들 너무", false, false, true, category, 1, 1)
        val bucketItem6 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", false, false, false, category, 1, 1)


        val bucketItemList = ArrayList<BucketItem>()

        bucketItemList.add(bucketItem1)
        bucketItemList.add(bucketItem2)
        bucketItemList.add(bucketItem3)
        bucketItemList.add(bucketItem4)
        bucketItemList.add(bucketItem5)
        bucketItemList.add(bucketItem6)

        val bucketList = BucketList(bucketItemList, false, 200)

        return bucketList
    }*/
}