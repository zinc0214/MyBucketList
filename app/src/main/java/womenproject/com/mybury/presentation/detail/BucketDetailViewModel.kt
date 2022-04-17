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
import womanproject.com.mybury.domain.usecase.detail.DeleteBucketUseCase
import womanproject.com.mybury.domain.usecase.detail.LoadBucketDetailItemUseCase
import womenproject.com.mybury.data.BucketRequest
import womenproject.com.mybury.data.StatusChangeBucketRequest
import womenproject.com.mybury.data.model.BucketDetailItem
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.data.model.UserIdRequest
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by HanAYeon on 2018. 11. 30..
 */

@HiltViewModel
class BucketDetailViewModel @Inject constructor(
    private val loadBucketDetailItemUseCase: LoadBucketDetailItemUseCase,
    private val deleteBucketUseCase: DeleteBucketUseCase
) : BaseViewModel() {

    private val _loadBucketDetail = MutableLiveData<BucketDetailItem>()
    val loadBucketDetail: LiveData<BucketDetailItem> = _loadBucketDetail

    private val _loadBucketState = MutableLiveData<LoadState>()
    val loadBucketState: LiveData<LoadState> = _loadBucketState

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _isDeleteSuccess = MutableLiveData<Boolean>()
    val isDeleteSuccess: LiveData<Boolean> = _isDeleteSuccess

    private val _isReDoSuccess = MutableLiveData<Boolean>()
    val isReDoSuccess: LiveData<Boolean> = _isReDoSuccess

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

    fun deleteBucketListener(bucketId: String) {
        if (accessToken == null || userId == null) {
            _showLoading.value = false
            _isDeleteSuccess.value = false
            return
        }

        val userIdRequest = UserIdRequest(userId)
        _showLoading.value = true

        viewModelScope.launch {
            runCatching {
                deleteBucketUseCase(accessToken, userIdRequest, bucketId).apply {
                    when (this@apply.retcode) {
                        "200" -> {
                            _isDeleteSuccess.value = true
                        }
                        "301" -> {
                            getRefreshToken {
                                _isDeleteSuccess.value = false
                            }
                        }
                    }
                }
            }.getOrElse {
                _showLoading.value = false
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