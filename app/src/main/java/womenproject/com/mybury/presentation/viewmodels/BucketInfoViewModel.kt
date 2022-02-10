package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.domain.usecase.home.LoadHomeBucketListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.data.toBucketData
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by HanAYeon on 2019-06-25.
 */

@HiltViewModel
class BucketInfoViewModel @Inject constructor(
    private val loadHomeBucketListUseCase: LoadHomeBucketListUseCase
) : BaseViewModel() {

    private val _bucketListLoadState = MutableLiveData<LoadState>()
    val bucketListLoadState: LiveData<LoadState> = _bucketListLoadState

    private val _homeBucketList = MutableLiveData<BucketList>()
    val homeBucketList: LiveData<BucketList> = _homeBucketList

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
                        }
                        Log.e("ayhan", "_homeBucketList : ${_homeBucketList.value}")
                    }
                }
            }.getOrElse {
                _bucketListLoadState.value = LoadState.FAIL
            }
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
}