package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

/**
 * Created by HanAYeon on 2019-06-25.
 */

class BucketInfoViewModel : BaseViewModel() {


    @SuppressLint("CheckResult")
    fun getMainBucketList(callback: MoreCallBackAnyList, filter: String, sort: String) {
        callback.start()
        apiInterface.requestHomeBucketList(accessToken, userId, filter, sort)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    when {
                        response.retcode == "200" -> {
                            Log.e("ayhan", "getMainBucketList:${response.retcode}")
                            callback.success(response.bucketlists)
                        }
                        response.retcode == "301" -> getRefreshToken(object : SimpleCallBack {
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
    fun getBucketListByCategory(callback: MoreCallBackAnyList, categoryId: String) {

        callback.start()

        apiInterface.requestCategoryBucketList(accessToken, categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { response ->
                    when {
                        response.retcode == "200" -> response.bucketlists
                        response.retcode == "301" -> getRefreshToken(object : SimpleCallBack {
                            override fun success() {
                                callback.restart()
                            }

                            override fun fail() {
                                callback.fail()
                            }

                        })
                        else -> callback.fail()
                    }
                }
                .subscribe({ bucketItemList ->
                    callback.success(bucketItemList as List<Any>)
                }) {
                    Log.e("ayhan", it.toString())
                    callback.fail()
                }


    }

    @SuppressLint("CheckResult")
    fun getCategoryList(callback: MoreCallBackAnyList) {

        apiInterface.requestBeforeWrite(accessToken, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    when {
                        response.retcode == "200" -> {
                            callback.success(response.categoryList)
                        }
                        response.retcode == "301" -> getRefreshToken(object : SimpleCallBack {
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
                    callback.fail()
                    Log.e("ayhan", it.toString())
                }
    }


}