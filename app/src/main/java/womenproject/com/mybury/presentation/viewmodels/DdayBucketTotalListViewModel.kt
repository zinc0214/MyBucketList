package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.data.*
import womenproject.com.mybury.data.network.RetrofitInterface
import womenproject.com.mybury.data.network.bucketListApi

/**
 * Created by HanAYeon on 2019. 1. 16..
 */

class DdayBucketTotalListViewModel  : BaseViewModel() {


    private var bucketList : BucketList? = null
    var progressVisible = ObservableInt(View.GONE)


    interface OnDdayBucketListGetEvent {
        fun start()
        fun finish(bucketList: BucketList?)
    }


    @SuppressLint("CheckResult")
    fun getDdayEachBucketList(api:String, callback: OnDdayBucketListGetEvent) {

        callback.start()

        bucketListApi.requestDdayBucketListResult()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { bucketList -> callback.finish(bucketList)}
    }

/*    fun getDdayEachBucketLis2t(api:String, callback: OnDdayBucketListGetEvent): BucketList? {

        callback.start()

        val restClient: RetrofitInterface = OkHttp3RetrofitManager(api).getRetrofitService(RetrofitInterface::class.java)

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

    }*/

}