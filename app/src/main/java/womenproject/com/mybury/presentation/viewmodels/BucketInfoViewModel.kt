package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

/**
 * Created by HanAYeon on 2019-06-25.
 */

class BucketInfoViewModel : BaseViewModel() {


    interface OnBucketListGetEvent {
        fun start()
        fun finish(bucketList: List<BucketItem>)
        fun fail()
    }


    @SuppressLint("CheckResult")
    fun getMainBucketList(callback: OnBucketListGetEvent, userId : String, token:String) {
        callback.start()
        apiInterface.requestHomeBucketList(token, userId, "started", "updateDt")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bucketList ->
                    run {
                        Log.e("ayhan", "getMainBucketList:${bucketList.retcode}")
                        callback.finish(bucketList.bucketlists)
                    }
                }) {
                    Log.e("ayhan", it.toString())
                    callback.fail()
                }

    }

    @SuppressLint("CheckResult")
    fun getMainBucketListByCategory(callback: OnBucketListGetEvent, categoryName: String) {

        callback.start()

        apiInterface.requestHomeBucketList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { response -> response.bucketlists }
                .subscribe({ bucketItemList ->
                    bucketItemList.filter { it.category.name == categoryName }
                    callback.finish(bucketItemList)
                }) {
                    Log.e("ayhan", it.toString())
                    callback.fail()
                }


    }


/*

    fun getMainBucketList2(api: String, callback: OnBucketListGetEvent): BucketList? {

        callback.start()

        val restClient: RetrofitInterface = OkHttp3RetrofitManager(api).getRetrofitService(RetrofitInterface::class.java)

        val bucketListResultData = restClient.requestHomeBucketList()
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