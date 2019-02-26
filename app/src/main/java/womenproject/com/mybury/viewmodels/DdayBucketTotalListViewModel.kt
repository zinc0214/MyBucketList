package womenproject.com.mybury.viewmodels

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import womenproject.com.mybury.R
import womenproject.com.mybury.data.*
import womenproject.com.mybury.network.OkHttp3RetrofitManager
import womenproject.com.mybury.network.RetrofitInterface

/**
 * Created by HanAYeon on 2019. 1. 16..
 */

class DdayBucketTotalListViewModel internal constructor(private val context: Context?) : BaseViewModel() {

    private val BUCKETLIST_API = context!!.resources.getString(R.string.bucket_list_api)
    private var bucketList : BucketList? = null
    var progressVisible = ObservableInt(View.GONE)


    interface OnDdayBucketListGetEvent {
        fun start()
        fun finish(bucketList: BucketList?)
    }

    fun getDdayEachBucketList(callback: OnDdayBucketListGetEvent): BucketList? {

        callback.start()

        val restClient: RetrofitInterface = OkHttp3RetrofitManager(BUCKETLIST_API).getRetrofitService(RetrofitInterface::class.java)

        val bucketListResultData = restClient.requestDdayBucketListResult()
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

    fun getDdayEachBucketItem(): BucketList {
        val category = BucketCategory("id", "운동")
        val bucketItem1 = BucketItem("bucketlist02", "퇴사하고 한달동안 보드게임하기.", false, false, false, category, 2, 5, 2)
        val bucketItem2 = BucketItem("bucketlist02", "신전떡볶이 오뎅쿠폰 10장 모으기.", false, false, false ,category, 0, 1, 2)
        val bucketItem3 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", false, false, true, category, 2, 5, 4)


        val bucketItemList = ArrayList<BucketItem>()

        bucketItemList.add(bucketItem1)
        bucketItemList.add(bucketItem2)
        bucketItemList.add(bucketItem3)

        val bucketList = BucketList(bucketItemList, false, 200)
        return bucketList
    }
}