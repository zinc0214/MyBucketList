package womenproject.com.mybury.presentation.detail

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
import womenproject.com.mybury.data.BucketRequest
import womenproject.com.mybury.data.StatusChangeBucketRequest
import womenproject.com.mybury.data.UseUserIdRequest
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

/**
 * Created by HanAYeon on 2018. 11. 30..
 */

class BucketDetailViewModel : BaseViewModel() {

    private val _showLoading = MutableLiveData<Boolean>()
    private val _isDeleteSuccess = MutableLiveData<Boolean>()
    private val _isReDoSuccess = MutableLiveData<Boolean>()

    val showLoading: LiveData<Boolean> = _showLoading
    val isDeleteSuccess: LiveData<Boolean> = _isDeleteSuccess
    val isReDoSuccess: LiveData<Boolean> = _isReDoSuccess

    @SuppressLint("CheckResult")
    fun loadBucketDetail(callback: MoreCallBackAny, bucketId: String) {
        callback.start()
        if(accessToken==null || userId==null) {
            callback.fail()
            return
        }

        apiInterface.requestDetailBucketList(accessToken, bucketId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ detailBucketItem ->
                    when (detailBucketItem.retcode) {
                        "200" -> {
                            callback.success(detailBucketItem)
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
                    Log.e("myBury", "loadBucketDetail Fail : $it")
                    callback.fail()
                }

    }

    @SuppressLint("CheckResult")
    fun setBucketComplete(callback: Simple3CallBack, bucketId: String) {
        val bucketRequest = BucketRequest(bucketId)
        if(accessToken==null) {
            callback.fail()
            return
        }

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
        if(accessToken==null) {
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

    fun deleteBucketListener(userId: UseUserIdRequest, bucketId: String) {
        if(accessToken==null) {
            _showLoading.value = false
            _isDeleteSuccess.value = false
            return
        }

        _showLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                apiInterface.deleteBucket(accessToken, userId, bucketId).apply {
                    withContext(Dispatchers.Main) {
                        when (this@apply.retcode) {
                            "200" -> _isDeleteSuccess.value = true
                            "301" -> getRefreshToken(object : SimpleCallBack {
                                override fun success() {
                                    deleteBucketListener(userId, bucketId)
                                }

                                override fun fail() {
                                    _isDeleteSuccess.value = false
                                }
                            })
                            else -> _isDeleteSuccess.value = false
                        }
                        _showLoading.value = false
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }


    fun redoBucketList(bucketId: String) {
        if(accessToken==null) {
            _showLoading.value = false
            _isReDoSuccess.value = false
            return
        }

        _showLoading.value = true
        val bucketRequest = StatusChangeBucketRequest(userId, bucketId)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                apiInterface.postRedoBucket(accessToken, bucketRequest).apply {
                    withContext(Dispatchers.Main) {
                        _isReDoSuccess.value = this@apply.retcode == "200"
                        _showLoading.value = false
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}