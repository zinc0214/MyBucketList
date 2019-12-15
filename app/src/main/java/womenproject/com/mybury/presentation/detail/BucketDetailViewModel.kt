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

    @SuppressLint("CheckResult")
    fun loadBucketDetail(callback: MoreCallBackAny, bucketId: String) {
        callback.start()
        apiInterface.requestDetailBucketList(accessToken, bucketId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ detailBucketItem ->
                    when {
                        detailBucketItem.retcode == "200" -> {
                            Log.e("ayhan", "getMainBucketList:${detailBucketItem.retcode}")
                            callback.success(detailBucketItem)
                        }
                        detailBucketItem.retcode == "301" ->getRefreshToken(object : SimpleCallBack {
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
                    Log.e("ayhan", it.toString())
                    callback.fail()
                }

    }

    @SuppressLint("CheckResult")
    fun setBucketComplete(callback: Simple3CallBack, bucketId: String) {

        val bucketRequest = BucketRequest(bucketId)
        callback.start()
        apiInterface.postCompleteBucket(accessToken, bucketRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ detailBucketItem ->
                    when {
                        detailBucketItem.retcode == "200" -> {
                            Log.e("ayhan", "getMainBucketList:${detailBucketItem.retcode}")
                            callback.success()
                        }
                        detailBucketItem.retcode == "301" ->getRefreshToken(object : SimpleCallBack {
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
                    Log.e("ayhan", it.toString())
                    callback.fail()
                }

    }

    @SuppressLint("CheckResult")
    fun deleteBucketListner(callback: Simple3CallBack, userId: UseUserIdRequest, bucketId: String) {
        callback.start()
        apiInterface.deleteBucket(accessToken, userId, bucketId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ detailBucketItem ->
                    when {
                        detailBucketItem.retcode == "200" -> {
                            Log.e("ayhan", "getMainBucketList:${detailBucketItem.retcode}")
                            callback.success()
                        }
                        detailBucketItem.retcode == "301" -> getRefreshToken(object : SimpleCallBack {
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
                    Log.e("ayhan", it.toString())
                    callback.fail()
                }

    }


}