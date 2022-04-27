package womenproject.com.mybury.presentation.write

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import womanproject.com.mybury.domain.usecase.detail.AddBucketItemUseCase
import womanproject.com.mybury.domain.usecase.detail.UpdateBucketItemUseCase
import womenproject.com.mybury.BuildConfig
import womenproject.com.mybury.data.model.AddBucketItemContent
import womenproject.com.mybury.data.model.AddBucketItemInfo
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.data.model.UpdateBucketItemInfo
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class BucketWriteViewModel @Inject constructor(
    private val addBucketItemUseCase: AddBucketItemUseCase,
    private val updateBucketItemUseCase: UpdateBucketItemUseCase
) : BaseViewModel() {

    val isOpenVisible = BuildConfig.DEBUG

    private val _addBucketLoadState = MutableLiveData<LoadState>()
    val addBucketLoadState: LiveData<LoadState> = _addBucketLoadState

    private val _updateBucketLoadState = MutableLiveData<LoadState>()
    val updateBucketLoadState: LiveData<LoadState> = _updateBucketLoadState

    fun uploadBucketList(
        bucketItem: AddBucketItemContent,
        imgList: MutableList<Any?>
    ) {

        if (accessToken == null || userId == null) {
            _addBucketLoadState.value = LoadState.FAIL
            return
        }

        val addBucketItemInfo = AddBucketItemInfo(
            content = bucketItem,
            imgList = imgList,
            userId = userId,
            token = accessToken
        )

        viewModelScope.launch {
            runCatching {
                _addBucketLoadState.value = LoadState.START
                addBucketItemUseCase.invoke(addBucketItemInfo).apply {
                    when (this.retcode) {
                        "200" -> {
                            _addBucketLoadState.value = LoadState.SUCCESS
                        }
                        "301" -> getRefreshToken(object : SimpleCallBack {
                            override fun success() {
                                _addBucketLoadState.value = LoadState.RESTART
                            }

                            override fun fail() {
                                _addBucketLoadState.value = LoadState.FAIL
                            }
                        })
                        else -> _addBucketLoadState.value = LoadState.FAIL
                    }
                }
            }.getOrElse {
                _addBucketLoadState.value = LoadState.FAIL
            }
        }
    }

    fun updateBucketList(
        bucketId: String,
        bucketItem: AddBucketItemContent,
        alreadyImgList: MutableMap<Int, String?>,
        imgList: ArrayList<Any?>
    ) {

        if (accessToken == null || userId == null) {
            _updateBucketLoadState.value = LoadState.FAIL
            return
        }

        _updateBucketLoadState.value = LoadState.START

        val updateBucketItemInfo = UpdateBucketItemInfo(
            bucketId = bucketId,
            content = bucketItem,
            imgList = imgList,
            alreadyImgList = alreadyImgList,
            userId = userId,
            token = accessToken
        )

        viewModelScope.launch {
            runCatching {
                updateBucketItemUseCase.invoke(updateBucketItemInfo).apply {
                    when (this.retcode) {
                        "200" -> {
                            _updateBucketLoadState.value = LoadState.SUCCESS
                        }
                        "301" -> getRefreshToken(object : SimpleCallBack {
                            override fun success() {
                                _updateBucketLoadState.value = LoadState.RESTART
                            }

                            override fun fail() {
                                _updateBucketLoadState.value = LoadState.FAIL
                            }
                        })
                        else -> _updateBucketLoadState.value = LoadState.FAIL
                    }
                }
            }.getOrElse {
                _updateBucketLoadState.value = LoadState.FAIL
            }
        }
    }
}