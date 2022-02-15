package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by HanAYeon on 2019. 1. 16..
 */

@HiltViewModel
class DdayBucketTotalListViewModel @Inject constructor() : BaseViewModel() {

    @SuppressLint("CheckResult")
    fun getDdayEachBucketList(callback: MoreCallBackAnyList, filter: String) {
        if (accessToken == null || userId == null) {
            callback.fail()
            return
        }

        callback.start()

        apiInterface.requestDdayBucketListResult(accessToken, userId, filter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                when (response.retcode) {
                    "200" -> callback.success(response.dDayBucketlists)
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
                Log.e("myBury", it.toString())
                callback.fail()
            }
    }
}