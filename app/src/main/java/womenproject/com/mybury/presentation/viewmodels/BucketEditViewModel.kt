package womenproject.com.mybury.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

class BucketEditViewModel : BaseViewModel() {

    private val _loadState = MutableLiveData<LoadState>()
    val loadState: LiveData<LoadState> = _loadState

    private val _allBucketResult = MutableLiveData<List<BucketItem>>()
    val allBucketResult: LiveData<List<BucketItem>> = _allBucketResult

    fun getMainBucketList(filter: String, sort: String) {
        if (accessToken == null || userId == null) {
            return
        }
        _loadState.value = LoadState.START

        viewModelScope.launch(Dispatchers.IO) {
            try {
                apiInterface.requestSortBucketList(accessToken, userId, filter, sort).apply {
                    withContext(Dispatchers.Main) {
                        when (this@apply.retcode) {
                            "200" -> {
                                _loadState.value = LoadState.SUCCESS
                                _allBucketResult.value = this@apply.bucketlists
                            }
                            "301" -> {
                                getRefreshToken(object : SimpleCallBack {
                                    override fun success() {
                                        _loadState.value = LoadState.RESTART
                                    }

                                    override fun fail() {
                                        _loadState.value = LoadState.FAIL
                                    }

                                })
                            }
                            else -> {
                                _loadState.value = LoadState.FAIL
                            }
                        }
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}