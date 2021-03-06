package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.MyPageInfo
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.util.fileToMultipartFile
import womenproject.com.mybury.util.stringToMultipartFile
import java.io.File

/**
 * Created by HanAYeon on 2019. 4. 23..
 */

class MyPageViewModel : BaseViewModel() {


    private val _myPageInfo = MutableLiveData<MyPageInfo>()

    @SuppressLint("CheckResult")
    fun getMyPageData(callback: MoreCallBackAny) {

        if(accessToken==null || userId==null) {
            callback.fail()
            return
        }
        callback.start()

        apiInterface.loadMyPageData(accessToken, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    callback.fail()
                }
                .subscribe({ response ->
                    when (response.retcode) {
                        "200" -> {
                            _myPageInfo.value = response
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
                    callback.fail()
                }
    }


    @SuppressLint("CheckResult")
    fun setProfileData(callback: Simple3CallBack, _nickName: String, _profileImg: File?, _useDefaultImg: Boolean) {

        if(accessToken==null || userId==null) {
            callback.fail()
            return
        }

        callback.start()
        val p_userId = userId.stringToMultipartFile("userId")
        val nickName = _nickName.stringToMultipartFile("name")
        val defaultImg = _useDefaultImg.stringToMultipartFile("defaultImg")
        val profileImg = _profileImg?.fileToMultipartFile("multipartFile")

        if (_profileImg == null) {
            apiInterface.postCreateProfile(accessToken, p_userId, nickName, defaultImg)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        when (response.retcode) {
                            "200" -> callback.success()
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
                        Log.e("myBury", "createAccountFail: $it")
                        callback.fail()
                    }
        } else {
            apiInterface.postCreateProfile(accessToken, p_userId, nickName, profileImg!!, defaultImg)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        when (response.retcode) {
                            "200" -> callback.success()
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
                        Log.e("myBury", "createAccountFail: $it")
                        callback.fail()
                    }
        }

    }

}