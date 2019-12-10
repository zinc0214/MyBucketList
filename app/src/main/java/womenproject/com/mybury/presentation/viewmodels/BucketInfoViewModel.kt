package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Category
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
    fun getMainBucketList(callback: OnBucketListGetEvent, userId: String, token: String, filter: String, sort: String) {
        callback.start()
        apiInterface.requestHomeBucketList(token, userId, filter, sort)
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
    fun getBucketListByCategory(callback: OnBucketListGetEvent, token : String, categoryId: String) {

        callback.start()

        apiInterface.requestCategoryBucketList(token, categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { response -> response.bucketlists }
                .subscribe({ bucketItemList ->
                    callback.finish(bucketItemList)
                }) {
                    Log.e("ayhan", it.toString())
                    callback.fail()
                }


    }

    interface GetBucketListCallBackListener {
        fun start()
        fun success(categoryList: List<Category>)
        fun fail()
    }

    @SuppressLint("CheckResult")
    fun getCategoryList(callback: GetBucketListCallBackListener, userId: String) {

        apiInterface.requestBeforeWrite(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.retcode == "200") {
                        Log.e("ayhan", "categoryLL : $response")
                        callback.success(response.categoryList)
                    }
                }) {
                    callback.fail()
                    Log.e("ayhan", it.toString())
                }
    }


}