package womenproject.com.mybury.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.domain.usecase.category.LoadCategoryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.data.toCategoryData
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val loadCategoryListUseCase: LoadCategoryListUseCase
) : BaseViewModel() {

    private val _categoryLoadState = MutableLiveData<LoadState>()
    val categoryLoadState: LiveData<LoadState> = _categoryLoadState

    private val _categoryList = MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>> = _categoryList

    fun getCategoryList() {
        if (accessToken == null || userId == null) {
            _categoryLoadState.value = LoadState.START
            return
        }

        viewModelScope.launch {
            runCatching {
                loadCategoryListUseCase.invoke(accessToken, userId).apply {
                    Log.e("ayhan", "response : ${this}")
                    when (this@apply.retcode) {
                        "200" -> {
                            _categoryLoadState.value = LoadState.SUCCESS
                            _categoryList.value = this@apply.categoryList.toCategoryData()
                        }
                        "301" -> {
                            getRefreshToken {
                                _categoryLoadState.value = it
                            }
                        }
                    }
                }
            }.getOrElse {
                Log.e("ayhan", "error : ${it.message}")
                _categoryLoadState.value = LoadState.FAIL
            }
        }
    }
}