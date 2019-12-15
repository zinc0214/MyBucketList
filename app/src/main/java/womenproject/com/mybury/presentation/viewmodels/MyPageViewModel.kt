package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
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

    val myPageInfo : LiveData<MyPageInfo> get() = _myPageInfo

    interface GetMyPageInfoListener {
        fun start()
        fun success(myPageInfo : MyPageInfo)
        fun fail()
    }


    @SuppressLint("CheckResult")
    fun getMyPageData(callback: GetMyPageInfoListener, token : String, userId: String) {

        callback.start()

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
                        _myPageInfo.value = response
                        callback.success(response)

                    }
                }) {
                    callback.fail()
                    Log.e("ayhan", it.toString())
                }
    }


    interface SetMyProfileListener {
        fun start()
        fun success()
        fun fail()
    }

    @SuppressLint("CheckResult")
    fun setProfileData(callback : SetMyProfileListener, token: String, _userId: String, _nickName : String, _profileImg : File?, _useDefaultImg : Boolean) {

        callback.start()
        val userId = _userId.stringToMultipartFile("userId")
        val nickName = _nickName.stringToMultipartFile("name")
        val defaultImg = _useDefaultImg.stringToMultipartFile("defaultImg")
        val profileImg = _profileImg?.fileToMultipartFile("multipartFile")

        if(_profileImg == null) {
            apiInterface.postCreateProfile(token, userId, nickName, defaultImg)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        Log.e("ayhan", "createAccountResponse:${response.retcode}")
                        if (response.retcode == "200") {
                            callback.success()
                        } else {
                            callback.fail()
                        }
                    }) {
                        Log.e("ayhan", "createAccountFail: $it")
                        callback.fail()
                    }
        } else {
            apiInterface.postCreateProfile(token, userId, nickName, profileImg!!, defaultImg)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        Log.e("ayhan", "createAccountResponse:${response.retcode}")
                        if (response.retcode == "200") {
                            callback.success()
                        } else {
                            callback.fail()
                        }
                    }) {
                        Log.e("ayhan", "createAccountFail: $it")
                        callback.fail()
                    }
        }

    }

}