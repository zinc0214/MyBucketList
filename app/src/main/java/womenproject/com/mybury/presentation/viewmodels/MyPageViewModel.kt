package womenproject.com.mybury.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import womanproject.com.mybury.domain.usecase.my.LoadMyPageInfoUseCase
import womanproject.com.mybury.domain.usecase.my.UpdateProfileUseCase
import womenproject.com.mybury.data.MyPageInfo
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.data.parseToCategoryInfos
import womenproject.com.mybury.presentation.base.BaseViewModel
import java.io.File
import javax.inject.Inject

/**
 * Created by HanAYeon on 2019. 4. 23..
 */

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val loadMyPageInfoUseCase: LoadMyPageInfoUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : BaseViewModel() {

    private val _myPageInfo = MutableLiveData<MyPageInfo>()
    val myPageInfo: LiveData<MyPageInfo> get() = _myPageInfo

    private val _loadMyPageInfoEvent = MutableLiveData<LoadState>()
    val loadMyPageInfoEvent: LiveData<LoadState> get() = _loadMyPageInfoEvent

    private val _updateProfileEvent = MutableLiveData<LoadState>()
    val updateProfileEvent: LiveData<LoadState> get() = _updateProfileEvent

    fun getMyPageData() {

        if (accessToken == null || userId == null) {
            _loadMyPageInfoEvent.value = LoadState.FAIL
            return
        }
        _loadMyPageInfoEvent.value = LoadState.START

        viewModelScope.launch {
            runCatching {
                loadMyPageInfoUseCase.invoke(accessToken, userId).apply {
                    when (this.retcode) {
                        "200" -> {
                            _myPageInfo.value = MyPageInfo(
                                name = this.name,
                                imageUrl = this.imageUrl,
                                startedCount = this.startedCount,
                                completedCount = this.completedCount,
                                dDayCount = this.dDayCount,
                                categoryList = this.categoryList.parseToCategoryInfos()
                            )
                            _loadMyPageInfoEvent.value = LoadState.SUCCESS
                        }
                        "301" -> getRefreshToken {
                            _loadMyPageInfoEvent.value = it
                        }
                        else -> _loadMyPageInfoEvent.value = LoadState.FAIL
                    }
                }
            }
        }
    }

    fun updateProfileData(
        _token: String = "",
        _userId: String = "",
        _nickName: String,
        _useDefaultImg: Boolean,
        _profileImg: File?,
    ) {

        val useableToken = if (_token.isNotBlank() && _token.isNotEmpty()) _token else accessToken
        val useableUserid = if (_userId.isNotBlank() && _userId.isNotEmpty()) _userId else userId

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