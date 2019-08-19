package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.network.RetrofitInterface
import womenproject.com.mybury.data.network.bucketListApi

/**
 * Created by HanAYeon on 2019-06-25.
 */

class BucketInfoViewModel : BaseViewModel() {


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

/*

    fun getMainBucketList2(api: String, callback: OnBucketListGetEvent): BucketList? {

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
*/


}