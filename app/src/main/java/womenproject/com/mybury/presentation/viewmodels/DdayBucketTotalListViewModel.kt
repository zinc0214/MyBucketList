package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.DdayBucketList
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

/**
 * Created by HanAYeon on 2019. 1. 16..
 */

class DdayBucketTotalListViewModel  : BaseViewModel() {


    @SuppressLint("CheckResult")
    fun getDdayEachBucketList(callback: MoreCallBackAnyList) {
        callback.start()

        apiInterface.requestDdayBucketListResult(accessToken, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    when {
                        response.retcode == "200" -> callback.success(response.dDayBucketlists)
                        response.retcode == "301" -> getRefreshToken(object : SimpleCallBack {
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
                    Log.e("ayhan", it.toString())
                    callback.fail()
                }

    }

}