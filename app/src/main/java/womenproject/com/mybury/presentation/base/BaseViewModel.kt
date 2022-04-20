package womenproject.com.mybury.presentation.base

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.MyBuryApplication.Companion.getAppContext
import womenproject.com.mybury.data.NewTokenRequest
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
import womenproject.com.mybury.data.Preference.Companion.getRefreshToken
import womenproject.com.mybury.data.Preference.Companion.getUserId
import womenproject.com.mybury.data.Preference.Companion.setAccessToken
import womenproject.com.mybury.data.Preference.Companion.setRefreshToken
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.data.network.apiInterface
import javax.inject.Inject

/**
 * Created by HanAYeon on 2019. 3. 7..
 */

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val accessToken = getAccessToken(getAppContext())
    private val refreshToken = getRefreshToken(getAppContext())
    val userId = getUserId(getAppContext())

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun isNetworkDisconnect(): Boolean {
        val cm =
            MyBuryApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        return !isConnected
    }

    fun setAccessToken(accessToken: String, refreshToken: String) {
        setAccessToken(getAppContext(), accessToken)
        setRefreshToken(getAppContext(), refreshToken)
    }

    interface SimpleCallBack {
        fun success()
        fun fail()
    }

    interface Simple3CallBack {
        fun start()
        fun success()
        fun fail()
        fun restart()
    }

    interface MoreCallBackAny {
        fun start()
        fun success(value: Any)
        fun fail()
        fun restart()
    }

    interface MoreCallBackAnyList {
        fun start()
        fun success(value: List<Any>)
        fun fail()
        fun restart()
    }

    @SuppressLint("CheckResult")
    fun getRefreshToken(a2CallBack: SimpleCallBack) {
        val newTokenRequest = NewTokenRequest(userId, refreshToken)
        apiInterface.getRefershToken(newTokenRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.retcode == "200") {
                    setAccessToken(getAppContext(), response.accessToken)
                    setRefreshToken(getAppContext(), response.refreshToken)
                }
            }) {
                a2CallBack.fail()
            }
    }

    @SuppressLint("CheckResult")
    fun getRefreshToken(result: (LoadState) -> Unit) {
        val newTokenRequest = NewTokenRequest(userId, refreshToken)
        apiInterface.getRefershToken(newTokenRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.retcode == "200") {
                    setAccessToken(getAppContext(), response.accessToken)
                    setRefreshToken(getAppContext(), response.refreshToken)
                    result(LoadState.RESTART)
                }
            }) {
                result(LoadState.FAIL)
            }
    }

    @SuppressLint("CheckResult")
    fun getRefreshToken() {
        val newTokenRequest = NewTokenRequest(userId, refreshToken)
        apiInterface.getRefershToken(newTokenRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.retcode == "200") {
                    setAccessToken(getAppContext(), response.accessToken)
                    setRefreshToken(getAppContext(), response.refreshToken)
                }
            }) {

            }
    }

}