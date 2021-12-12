package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

class BucketEditViewModel : BaseViewModel() {

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
}