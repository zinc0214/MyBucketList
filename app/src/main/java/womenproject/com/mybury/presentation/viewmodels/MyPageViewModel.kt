package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.MyPageInfo
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

/**
 * Created by HanAYeon on 2019. 4. 23..
 */

class MyPageViewModel : BaseViewModel() {

    interface GetMyPageInfoListener {
        fun start()
        fun success(myPageInfo : MyPageInfo)
        fun fail()
    }


    @SuppressLint("CheckResult")
    fun getMyPageData(callback: GetMyPageInfoListener, token : String, userId: String) {

        apiInterface.loadMyPageData(token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    callback.fail()
                }
                .subscribe({ response ->
                    Log.e("ayhan", "getMyPageInfo : ${response}")
                    if (response.retcode == "200") {
                        Log.e("ayhan", "categoryLL : $response")
                        callback.success(response)
                    }
                }) {
                    callback.fail()
                    Log.e("ayhan", it.toString())
                }
    }

}