package womenproject.com.mybury.presentation.detail

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.BuildConfig
import womenproject.com.mybury.data.BucketRequest
import womenproject.com.mybury.data.CancelBucketRequest
import womenproject.com.mybury.data.DetailBucketItem
import womenproject.com.mybury.data.UseUserIdRequest
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

/**
 * Created by HanAYeon on 2018. 11. 30..
 */

class BucketDetailViewModel : BaseViewModel() {

    val isOpenVisible = BuildConfig.DEBUG

    @SuppressLint("CheckResult")
    fun loadBucketDetail(callback: MoreCallBackAny, bucketId: String) {
        callback.start()
        apiInterface.requestDetailBucketList(accessToken, bucketId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ detailBucketItem ->
                    when {
                        detailBucketItem.retcode == "200" -> {
                            callback.success(detailBucketItem)
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
                    Log.e("myBury", "loadBucketDetail Fail : $it")
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

    @SuppressLint("CheckResult")
    fun setBucketCancel(callback: Simple3CallBack, bucketId: String) {
        val bucketRequest = CancelBucketRequest(userId, bucketId)
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

    @SuppressLint("CheckResult")
    fun deleteBucketListener(callback: Simple3CallBack, userId: UseUserIdRequest, bucketId: String) {
        callback.start()
        apiInterface.deleteBucket(accessToken, userId, bucketId)
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
                    Log.e("myBury", "deleteBucket Fail: $it")
                    callback.fail()
                }

    }


}