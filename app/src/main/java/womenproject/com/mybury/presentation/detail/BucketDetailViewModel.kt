package womenproject.com.mybury.presentation.detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import womanproject.com.mybury.domain.usecase.detail.LoadBucketDetailItemUseCase
import womenproject.com.mybury.data.BucketRequest
import womenproject.com.mybury.data.StatusChangeBucketRequest
import womenproject.com.mybury.data.UseUserIdRequest
import womenproject.com.mybury.data.model.BucketDetailItem
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by HanAYeon on 2018. 11. 30..
 */

@HiltViewModel
class BucketDetailViewModel @Inject constructor(
    private val loadBucketDetailItemUseCase: LoadBucketDetailItemUseCase
) : BaseViewModel() {

    private val _loadBucketDetail = MutableLiveData<BucketDetailItem>()
    private val _loadBucketState = MutableLiveData<LoadState>()

    private val _showLoading = MutableLiveData<Boolean>()
    private val _isDeleteSuccess = MutableLiveData<Boolean>()
    private val _isReDoSuccess = MutableLiveData<Boolean>()


    val loadBucketDetail: LiveData<BucketDetailItem> = _loadBucketDetail
    val loadBucketState : LiveData<LoadState> = _loadBucketState
    val showLoading: LiveData<Boolean> = _showLoading
    val isDeleteSuccess: LiveData<Boolean> = _isDeleteSuccess
    val isReDoSuccess: LiveData<Boolean> = _isReDoSuccess

    @SuppressLint("CheckResult")
    fun loadBucketDetail(bucketId: String) {

        if (accessToken == null || userId == null) {
            _loadBucketState.value = LoadState.FAIL
            return
        }

        _loadBucketState.value = LoadState.START

        viewModelScope.launch {
            runCatching {
                loadBucketDetailItemUseCase(accessToken, bucketId, userId).apply {
                    when (this@apply.retcode) {
                        "200" -> {
                            _loadBucketDetail.value = this@apply
                            _loadBucketState.value = LoadState.SUCCESS
                        }
                        "301" -> {
                            getRefreshToken {
                                _loadBucketState.value = it
                            }
                        }
                    }
                }
            }.getOrElse {
                _loadBucketState.value = LoadState.FAIL
            }
        }
    }

    @SuppressLint("CheckResult")
    fun setBucketComplete(callback: Simple3CallBack, bucketId: String) {
        val bucketRequest = BucketRequest(bucketId)
        if (accessToken == null) {
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

    fun deleteBucketListener(userId: UseUserIdRequest, bucketId: String) {
        if (accessToken == null) {
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
        if (accessToken == null) {
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