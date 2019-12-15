package womenproject.com.mybury.presentation.detail

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.BucketRequest
import womenproject.com.mybury.data.DetailBucketItem
import womenproject.com.mybury.data.UseUserIdRequest
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

/**
 * Created by HanAYeon on 2018. 11. 30..
 */

class BucketDetailViewModel : BaseViewModel() {


    interface OnBucketLoadEventListener {
        fun start()
        fun success(detailBucketItem: DetailBucketItem)
        fun fail()
    }


    @SuppressLint("CheckResult")
    fun loadBucketDetail(callback: OnBucketLoadEventListener, token: String, bucketId: String) {
        callback.start()
        apiInterface.requestDetailBucketList(token, bucketId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ detailBucketItem ->
                    if (detailBucketItem.retcode == "200") {
                        Log.e("ayhan", "getMainBucketList:${detailBucketItem.retcode}")
                        callback.success(detailBucketItem)
                    } else {
                        callback.fail()
                    }

                }) {
                    Log.e("ayhan", it.toString())
                    callback.fail()
                }

    }


    interface OnBucketCompleteEventListener {
        fun start()
        fun success()
        fun fail()
    }

    @SuppressLint("CheckResult")
    fun setBucketComplete(callback: OnBucketCompleteEventListener, token: String, bucketId: String) {

        val bucketRequest = BucketRequest(bucketId)
        callback.start()
        apiInterface.postCompleteBucket(token, bucketRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ detailBucketItem ->
                    if (detailBucketItem.retcode == "200") {
                        Log.e("ayhan", "getMainBucketList:${detailBucketItem.retcode}")
                        callback.success()
                    } else {
                        callback.fail()
                    }

                }) {
                    Log.e("ayhan", it.toString())
                    callback.fail()
                }

    }

    @SuppressLint("CheckResult")
    fun deleteBucketListner(callback: OnBucketCompleteEventListener, token: String, userId : UseUserIdRequest, bucketId: String) {
        callback.start()
        apiInterface.deleteBucket(token, userId, bucketId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ detailBucketItem ->
                    if (detailBucketItem.retcode == "200") {
                        Log.e("ayhan", "getMainBucketList:${detailBucketItem.retcode}")
                        callback.success()
                    } else {
                        callback.fail()
                    }

                }) {
                    Log.e("ayhan", it.toString())
                    callback.fail()
                }

    }



}