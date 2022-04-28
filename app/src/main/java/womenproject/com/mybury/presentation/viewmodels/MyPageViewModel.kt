package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import womanproject.com.mybury.domain.usecase.my.UpdateProfileUseCase
import womenproject.com.mybury.data.MyPageInfo
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel
import java.io.File
import javax.inject.Inject

/**
 * Created by HanAYeon on 2019. 4. 23..
 */

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase
) : BaseViewModel() {

    private val _myPageInfo = MutableLiveData<MyPageInfo>()
    private val _updateProfileEvent = MutableLiveData<LoadState>()
    val updateProfileEvent: LiveData<LoadState> get() = _updateProfileEvent

    @SuppressLint("CheckResult")
    fun getMyPageData(callback: MoreCallBackAny) {

        if (accessToken == null || userId == null) {
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

    fun updateProfileData(
        _token: String = "",
        _userId : String = "",
        _nickName: String,
        _useDefaultImg: Boolean,
        _profileImg: File?,
    ) {

        val useableToken = if (_token.isNotBlank() && _token.isNotEmpty()) _token else accessToken
        val useableUserid = if(_userId.isNotBlank() && _userId.isNotEmpty()) _userId else userId

        if (useableToken.isNullOrBlank() || useableUserid.isNullOrBlank()) {
            _updateProfileEvent.value = LoadState.FAIL
            return
        }

        _updateProfileEvent.value = LoadState.START

        viewModelScope.launch {
            runCatching {
                updateProfileUseCase.invoke(
                    useableToken, useableUserid, _nickName, _useDefaultImg, _profileImg
                ).apply {
                    when (this.retcode) {
                        "200" -> _updateProfileEvent.value = LoadState.SUCCESS
                        "301" -> getRefreshToken(object : SimpleCallBack {
                            override fun success() {
                                _updateProfileEvent.value = LoadState.RESTART
                            }

                            override fun fail() {
                                _updateProfileEvent.value = LoadState.FAIL
                            }
                        })
                        else -> _updateProfileEvent.value = LoadState.FAIL
                    }
                }
            }.getOrElse {
                _updateProfileEvent.value = LoadState.FAIL
            }
        }
    }

}