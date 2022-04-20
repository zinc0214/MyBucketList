package womenproject.com.mybury.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import womanproject.com.mybury.domain.usecase.category.LoadBucketListByCategoryUseCase
import womanproject.com.mybury.domain.usecase.detail.CompleteBucketUseCase
import womanproject.com.mybury.domain.usecase.home.CancelBucketItemUseCase
import womanproject.com.mybury.domain.usecase.home.LoadHomeBucketListUseCase
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.model.BucketRequest
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.data.model.StatusChangeBucketRequest
import womenproject.com.mybury.data.toBucketData
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by HanAYeon on 2019-06-25.
 */

@HiltViewModel
class BucketListViewModel @Inject constructor(
    private val loadHomeBucketListUseCase: LoadHomeBucketListUseCase,
    private val cancelBucketItemUseCase: CancelBucketItemUseCase,
    private val loadBucketListByCategoryUseCase: LoadBucketListByCategoryUseCase,
    private val completeBucketUseCase: CompleteBucketUseCase
) : BaseViewModel() {

    private val _bucketListLoadState = MutableLiveData<LoadState>()
    val bucketListLoadState: LiveData<LoadState> = _bucketListLoadState

    private val _homeBucketList = MutableLiveData<BucketList>()
    val homeBucketList: LiveData<BucketList> = _homeBucketList

    private val _bucketCancelLoadState = MutableLiveData<LoadState>()
    val bucketCancelLoadState: LiveData<LoadState> = _bucketCancelLoadState

    private val _categoryBucketList = MutableLiveData<BucketList>()
    val categoryBucketList: LiveData<BucketList> = _categoryBucketList

    private val _completeBucketState = MutableLiveData<Pair<Boolean, BucketItem?>>()
    val completeBucketState: LiveData<Pair<Boolean, BucketItem?>> = _completeBucketState

    fun getHomeBucketList(filter: String, sort: String) {
        if (accessToken == null || userId == null) {
            _bucketListLoadState.value = LoadState.FAIL
            return
        }

        _bucketListLoadState.value = LoadState.START

        viewModelScope.launch {
            runCatching {
                loadHomeBucketListUseCase.invoke(accessToken, userId, filter, sort).apply {
                    withContext(Dispatchers.Main) {
                        val response = this@apply
                        when (response.retcode) {
                            "200" -> {
                                _homeBucketList.value = BucketList(
                                    bucketlists = response.bucketlists.toBucketData(),
                                    popupYn = response.popupYn,
                                    retcode = response.retcode
                                )
                                _bucketListLoadState.value = LoadState.SUCCESS
                            }
                            "301" -> {
                                getRefreshToken {
                                    _bucketListLoadState.value = it
                                }
                            }
                            else -> {
                                _bucketListLoadState.value = LoadState.FAIL
                            }
                        }
                    }
                }
            }.getOrElse {
                _bucketListLoadState.value = LoadState.FAIL
            }
        }
    }

    fun bucketCancel(bucketId: String) {
        if (accessToken == null) {
            _bucketCancelLoadState.value = LoadState.FAIL
            return
        }

        viewModelScope.launch {
            runCatching {
                val bucketRequest = StatusChangeBucketRequest(userId, bucketId)
                cancelBucketItemUseCase.invoke(accessToken, bucketRequest).apply {
                    withContext(Dispatchers.Main) {
                        when (this@apply.retcode) {
                            "200" -> {
                                _bucketCancelLoadState.value = LoadState.SUCCESS
                            }
                            "301" -> {
                                _bucketCancelLoadState.value = LoadState.FAIL
                            }
                        }
                    }
                }
            }.getOrElse {
                _bucketCancelLoadState.value = LoadState.FAIL
            }
        }
    }

    fun getBucketListByCategory(categoryId: String) {
        if (accessToken == null) {
            _bucketListLoadState.value = LoadState.FAIL
            return
        }

        viewModelScope.launch {
            runCatching {
                loadBucketListByCategoryUseCase(accessToken, categoryId).apply {
                    withContext(Dispatchers.Main) {
                        val response = this@apply
                        when (response.retcode) {
                            "200" -> {
                                _categoryBucketList.value = BucketList(
                                    bucketlists = response.bucketlists.toBucketData(),
                                    popupYn = response.popupYn,
                                    retcode = response.retcode
                                )
                                _bucketListLoadState.value = LoadState.SUCCESS
                            }
                            "301" -> {
                                getRefreshToken {
                                    _bucketListLoadState.value = it
                                }
                            }
                            else -> {
                                _bucketListLoadState.value = LoadState.FAIL
                            }
                        }
                    }
                }
            }
        }
    }

    fun setBucketComplete(bucketInfo: BucketItem) {
        val bucketRequest = BucketRequest(bucketInfo.id)
        if (accessToken == null) {
            _completeBucketState.value = false to null
            return
        }

        viewModelScope.launch {
            runCatching {
                completeBucketUseCase(accessToken, bucketRequest).apply {
                    Log.e("ayhan", "response : ${this}")
                    when (this@apply.retcode) {
                        "200" -> {
                            _completeBucketState.value = true to bucketInfo
                        }
                        "301" -> getRefreshToken(object : SimpleCallBack {
                            override fun success() {
                                _completeBucketState.value = false to null
                            }

                            override fun fail() {
                                _completeBucketState.value = false to null
                            }
                        })
                        else -> _completeBucketState.value = false to null
                    }
                }
            }.getOrElse {
                _completeBucketState.value =  false to null
            }
        }
    }
}