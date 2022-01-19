package womenproject.com.mybury.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketListOrder
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

class BucketSortViewModel : BaseViewModel() {

    private val _bucketLoadState = MutableLiveData<LoadState>()
    val bucketLoadState: LiveData<LoadState> = _bucketLoadState

    private val _bucketUpdateState = MutableLiveData<LoadState>()
    val bucketUpdateState: LiveData<LoadState> = _bucketUpdateState

    private val _allBucketResult = MutableLiveData<List<BucketItem>>()
    val allBucketResult: LiveData<List<BucketItem>> = _allBucketResult

    fun getMainBucketList(filter: String, sort: String) {
        if (accessToken == null || userId == null) {
            return
        }
        _bucketLoadState.value = LoadState.START

        viewModelScope.launch(Dispatchers.IO) {
            try {
                apiInterface.requestSortBucketList(accessToken, userId, filter, sort).apply {
                    withContext(Dispatchers.Main) {
                        when (this@apply.retcode) {
                            "200" -> {
                                _bucketLoadState.value = LoadState.SUCCESS
                                _allBucketResult.value = this@apply.bucketlists
                            }
                            "301" -> {
                                getRefreshToken(object : SimpleCallBack {
                                    override fun success() {
                                        _bucketLoadState.value = LoadState.RESTART
                                    }

                                    override fun fail() {
                                        _bucketLoadState.value = LoadState.FAIL
                                    }
                                })
                            }
                            else -> {
                                _bucketLoadState.value = LoadState.FAIL
                            }
                        }
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _bucketLoadState.value = LoadState.FAIL
                }
            }
        }
    }

    fun updateBucketListOrder(changedBucketList: List<BucketItem>) {
        if (accessToken == null || userId == null) {
            return
        }
        _bucketUpdateState.value = LoadState.START

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bucketListOrder = BucketListOrder(
                    userId,
                    changedBucketList.map { it.id }
                )
                apiInterface.updateBucketListOrder(
                    accessToken,
                    bucketListOrder
                ).apply {
                    withContext(Dispatchers.Main) {
                        when (this@apply.retcode) {
                            "200" -> {
                                _bucketUpdateState.value = LoadState.SUCCESS
                            }
                            "301" -> {
                                getRefreshToken(object : SimpleCallBack {
                                    override fun success() {
                                        _bucketUpdateState.value = LoadState.RESTART
                                    }

                                    override fun fail() {
                                        _bucketUpdateState.value = LoadState.FAIL
                                    }
                                })
                            }
                            else -> {
                                _bucketUpdateState.value = LoadState.FAIL
                            }
                        }
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _bucketLoadState.value = LoadState.FAIL
                }
            }
        }
    }
}