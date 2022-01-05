package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.data.StatusChangeBucketRequest
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

/**
 * Created by HanAYeon on 2019-06-25.
 */

class BucketInfoViewModel : BaseViewModel() {

    private val _categoryLoadState = MutableLiveData<LoadState>()
    val categoryLoadState: LiveData<LoadState> = _categoryLoadState

    private val _categoryList = MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>> = _categoryList

    @SuppressLint("CheckResult")
    fun getMainBucketList(callback: MoreCallBackAny, filter: String, sort: String) {
        if (accessToken == null || userId == null) {
            callback.fail()
            return
        }
        callback.start()
        apiInterface.requestHomeBucketList(accessToken, userId, filter, sort)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                when (response.retcode) {
                    "200" -> {
                        callback.success(response)
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
                Log.e("myBury", "getMainBucketListFail : $it")
                callback.fail()
            }

    }


    @SuppressLint("CheckResult")
    fun setBucketCancel(callback: Simple3CallBack, bucketId: String) {
        if (accessToken == null) {
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

    @SuppressLint("CheckResult")
    fun getBucketListByCategory(callback: MoreCallBackAnyList, categoryId: String) {
        if (accessToken == null) {
            callback.fail()
            return
        }
        callback.start()

        apiInterface.requestCategoryBucketList(accessToken, categoryId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response ->
                when (response.retcode) {
                    "200" -> response.bucketlists
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
            }
            .subscribe({ bucketItemList ->
                callback.success(bucketItemList as List<Any>)
            }) {
                Log.e("myBury", "requestCategoryBucketList Fail :$it")
                callback.fail()
            }


    }

    @SuppressLint("CheckResult")
    fun getCategoryList() {
        if (accessToken == null || userId == null) {
            _categoryLoadState.value = LoadState.START
            return
        }

        viewModelScope.launch {
            try {
                apiInterface.requestBeforeWrite(accessToken, userId).apply {
                    withContext(Dispatchers.Main) {
                        when (this@apply.retcode) {
                            "200" -> {
                                _categoryList.value = this@apply.categoryList
                            }
                            "301" -> getRefreshToken(object : SimpleCallBack {
                                override fun success() {
                                    _categoryLoadState.value = LoadState.RESTART
                                }

                                override fun fail() {
                                    _categoryLoadState.value = LoadState.FAIL
                                }
                            })
                            else -> {
                                _categoryLoadState.value = LoadState.FAIL
                            }
                        }
                    }
                }
            } catch (e: Throwable) {
                _categoryLoadState.value = LoadState.FAIL
                Log.e("myBury", "getCategoryListFail : $e")
            }
        }
    }
}