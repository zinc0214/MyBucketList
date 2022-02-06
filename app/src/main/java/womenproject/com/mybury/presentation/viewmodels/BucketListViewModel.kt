package womenproject.com.mybury.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.data.model.StatusChangeBucketRequest
import com.zinc.domain.usecase.home.CancelBucketItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class BucketListViewModel @Inject constructor(
    private val cancelBucketItemUseCase: CancelBucketItemUseCase
) : BaseViewModel() {

    private val _bucketCancelLoadState = MutableLiveData<LoadState>()
    val bucketCancelLoadState: LiveData<LoadState> = _bucketCancelLoadState

    fun setBucketCancel(bucketId: String) {
        if (accessToken == null) {
            _bucketCancelLoadState.value = LoadState.FAIL
            return
        }

        viewModelScope.launch {
            runCatching {
                val bucketRequest = StatusChangeBucketRequest(userId, bucketId)
                cancelBucketItemUseCase.invoke(accessToken, bucketRequest).apply {
                    withContext(Dispatchers.Main) {
                        when (this@apply.retcode) {
                            "200" -> {
                                _bucketCancelLoadState.value = LoadState.SUCCESS
                            }
                            "301" -> {
                                _bucketCancelLoadState.value = LoadState.FAIL
                            }
                        }
                    }
                }
            }.getOrElse {
                _bucketCancelLoadState.value = LoadState.FAIL
            }
        }
    }
}