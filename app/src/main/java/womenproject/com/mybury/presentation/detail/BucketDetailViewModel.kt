package womenproject.com.mybury.presentation.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import womanproject.com.mybury.domain.usecase.detail.CompleteBucketUseCase
import womanproject.com.mybury.domain.usecase.detail.DeleteBucketUseCase
import womanproject.com.mybury.domain.usecase.detail.LoadBucketDetailItemUseCase
import womanproject.com.mybury.domain.usecase.detail.RedoBucketUseCase
import womenproject.com.mybury.data.model.*
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by HanAYeon on 2018. 11. 30..
 */

@HiltViewModel
class BucketDetailViewModel @Inject constructor(
    private val loadBucketDetailItemUseCase: LoadBucketDetailItemUseCase,
    private val deleteBucketUseCase: DeleteBucketUseCase,
    private val completeBucketUseCase: CompleteBucketUseCase,
    private val redoBucketUseCase: RedoBucketUseCase
) : BaseViewModel() {

    private val _loadBucketDetail = MutableLiveData<BucketDetailItem>()
    val loadBucketDetail: LiveData<BucketDetailItem> = _loadBucketDetail

    private val _loadBucketState = MutableLiveData<LoadState>()
    val loadBucketState: LiveData<LoadState> = _loadBucketState

    private val _completeBucketState = MutableLiveData<LoadState>()
    val completeBucketState: LiveData<LoadState> = _completeBucketState

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

    fun setBucketComplete(bucketId: String) {
        val bucketRequest = BucketRequest(bucketId)
        if (accessToken == null) {
            _completeBucketState.value = LoadState.FAIL
            return
        }

        _completeBucketState.value = LoadState.START

        viewModelScope.launch {
            runCatching {
                completeBucketUseCase(accessToken, bucketRequest).apply {
                    when (this@apply.retcode) {
                        "200" -> {
                            _completeBucketState.value = LoadState.SUCCESS
                        }
                        "301" -> getRefreshToken(object : SimpleCallBack {
                            override fun success() {
                                _completeBucketState.value = LoadState.RESTART
                            }

                            override fun fail() {
                                _completeBucketState.value = LoadState.FAIL
                            }
                        })
                        else -> _completeBucketState.value = LoadState.FAIL
                    }
                }
            }.getOrElse {
                _completeBucketState.value = LoadState.FAIL
            }
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

                    Log.e("myBury", "delete : $this")
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
            runCatching {
                redoBucketUseCase(accessToken, bucketRequest).apply {
                    withContext(Dispatchers.Main) {
                        _isReDoSuccess.value = this@apply.retcode == "200"
                        _showLoading.value = false
                    }
                }
            }.getOrElse {
                _showLoading.value = false
                _isReDoSuccess.value = false
            }
        }
    }
}