package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.StatusChangeBucketRequest
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

/**
 * Created by HanAYeon on 2019. 1. 16..
 */

class DdayBucketTotalListViewModel : BaseViewModel() {


    @SuppressLint("CheckResult")
    fun getDdayEachBucketList(callback: MoreCallBackAnyList, filter: String) {
        if (accessToken == null || userId == null) {
            callback.fail()
            return
        }

        callback.start()

        apiInterface.requestDdayBucketListResult(accessToken, userId, filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    when (response.retcode) {
                        "200" -> callback.success(response.dDayBucketlists)
                        "301" -> getRefreshToken(object : SimpleCallBack {
                            override fun success() {
                                callback.restart()
                            }

                            override fun fail() {
                                callback.fail()
                            }

                        })
                        else -> callback.fail()
                    }
                }) {
                    Log.e("myBury", it.toString())
                    callback.fail()
                }

    }

    @SuppressLint("CheckResult")
    fun setBucketCancel(callback: Simple3CallBack, bucketId: String) {
        if (accessToken == null || userId == null) {
            callback.fail()
            return
        }

        val bucketRequest = StatusChangeBucketRequest(userId, bucketId)
        callback.start()
        apiInterface.postCancelBucket(accessToken, bucketRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ detailBucketItem ->
                    when (detailBucketItem.retcode) {
                        "200" -> {
                            callback.success()
                        }
                        "301" -> getRefreshToken(object : SimpleCallBack {
                            override fun success() {
                                callback.restart()
                            }

                            override fun fail() {
                                callback.fail()
                            }

                        })
                        else -> callback.fail()
                    }

                }) {
                    Log.e("myBury", "postCompleteBucket Fail : $it")
                    callback.fail()
                }

    }

}