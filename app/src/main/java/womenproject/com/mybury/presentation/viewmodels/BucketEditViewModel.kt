package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

class BucketEditViewModel : BaseViewModel() {

    private val _apiState = MutableLiveData<ApiState>()
    val apiState: LiveData<ApiState> get() = _apiState

    private val _bucketList = MutableLiveData<List<BucketItem>>()
    val bucketList: LiveData<List<BucketItem>> get() = _bucketList


    @SuppressLint("CheckResult")
    fun getMainBucketList(callback: MoreCallBackAny) {
        if (accessToken == null || userId == null) {
            callback.fail()
            return
        }
        callback.start()
        apiInterface.requestHomeBucketList(accessToken, userId, "all", "updatedDt")
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
    fun getMainBucketList2() {
        if (accessToken == null || userId == null) {
            _apiState.value = ApiState.Fail
            return
        }
        _apiState.value = ApiState.Start
        apiInterface.requestHomeBucketList(accessToken, userId, "all", "updatedDt")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                when (response.retcode) {
                    "200" -> {
                        Log.e("ayhan", "code")
                        _apiState.value = ApiState.Success(response.bucketlists)
                        _bucketList.value = response.bucketlists
                    }
                    "301" -> getRefreshToken(object : SimpleCallBack {
                        override fun success() {
                            _apiState.value = ApiState.Restart
                        }

                        override fun fail() {
                            _apiState.value = ApiState.Fail
                        }

                    })
                    else -> _apiState.value = ApiState.Fail
                }
            }) {
                Log.e("myBury", "getMainBucketListFail : $it")
                _apiState.value = ApiState.Fail
            }

    }


}