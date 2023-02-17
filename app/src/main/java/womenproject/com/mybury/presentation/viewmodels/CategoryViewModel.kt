package womenproject.com.mybury.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import womanproject.com.mybury.domain.usecase.category.AddCategoryItemUseCase
import womanproject.com.mybury.domain.usecase.category.ChangeCategoryListUseCase
import womanproject.com.mybury.domain.usecase.category.EditCategoryItemNameUseCase
import womanproject.com.mybury.domain.usecase.category.LoadCategoryListUseCase
import womanproject.com.mybury.domain.usecase.category.RemoveCategoryItemUseCase
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.data.model.AddCategoryRequest
import womenproject.com.mybury.data.model.ChangeCategoryStatusRequest
import womenproject.com.mybury.data.model.EditCategoryNameRequest
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.data.model.RemoveCategoryRequest
import womenproject.com.mybury.data.toCategoryData
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val loadCategoryListUseCase: LoadCategoryListUseCase,
    private val addCategoryItemUseCase: AddCategoryItemUseCase,
    private val editCategoryItemNameUseCase: EditCategoryItemNameUseCase,
    private val changeCategoryListUseCase: ChangeCategoryListUseCase,
    private val removeCategoryItemUseCase: RemoveCategoryItemUseCase
) : BaseViewModel() {

    private val _categoryLoadState = MutableLiveData<LoadState>()
    val categoryLoadState: LiveData<LoadState> = _categoryLoadState

    private val _categoryList = MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>> = _categoryList

    private val _addCategoryItemState = MutableLiveData<LoadState>()
    val addCategoryItemState: LiveData<LoadState> = _addCategoryItemState

    private val _editCategoryItemNameState = MutableLiveData<LoadState>()
    val editCategoryItemNameState: LiveData<LoadState> = _editCategoryItemNameState

    private val _changeCategoryItemState = MutableLiveData<LoadState>()
    val changeCategoryItemState: LiveData<LoadState> = _changeCategoryItemState

    private val _removeCategoryItemState = MutableLiveData<LoadState>()
    val removeCategoryItemState: LiveData<LoadState> = _removeCategoryItemState

    fun loadCategoryList() {
        if (accessToken == null || userId == null) {
            _categoryLoadState.value = LoadState.FAIL
            return
        }

        _categoryLoadState.value = LoadState.START

        viewModelScope.launch {
            runCatching {
                loadCategoryListUseCase.invoke(accessToken, userId).apply {
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
                _categoryLoadState.value = LoadState.FAIL
            }
        }
    }

    fun removeCategoryItem(categoryIds: HashSet<String>) {
        if (accessToken == null || userId == null) {
            _removeCategoryItemState.value = LoadState.FAIL
            return
        }

        _removeCategoryItemState.value = LoadState.START

        val idList = categoryIds.map { it }
        val request = RemoveCategoryRequest(userId, idList)
        viewModelScope.launch {
            runCatching {
                removeCategoryItemUseCase.invoke(accessToken, request).apply {
                    when (this.retcode) {
                        "200" -> _removeCategoryItemState.value = LoadState.SUCCESS
                        "301" -> getRefreshToken { state ->
                            _removeCategoryItemState.value = state

                        }
                        else -> _removeCategoryItemState.value = LoadState.FAIL
                    }
                }

            }.getOrElse {
                _removeCategoryItemState.value = LoadState.FAIL
            }
        }
    }

    fun addCategoryItem(categoryName: String) {
        if (accessToken == null || userId == null) {
            _addCategoryItemState.value = LoadState.FAIL
            return
        }

        val request = AddCategoryRequest(userId, categoryName)

        viewModelScope.launch {
            _addCategoryItemState.value = LoadState.START
            runCatching {
                addCategoryItemUseCase.invoke(accessToken, request).apply {
                    when (this.retcode) {
                        "200" -> _addCategoryItemState.value = LoadState.SUCCESS
                        "301" -> getRefreshToken(object : SimpleCallBack {
                            override fun success() {
                                _addCategoryItemState.value = LoadState.RESTART
                            }

                            override fun fail() {
                                _addCategoryItemState.value = LoadState.FAIL
                            }
                        })
                        else -> _addCategoryItemState.value = LoadState.FAIL
                    }
                }
            }.getOrElse {
                _addCategoryItemState.value = LoadState.FAIL
            }
        }
    }

    fun editCategoryItem(category: Category, categoryName: String) {
        if (accessToken == null || userId == null) {
            Log.e("mybury", "edit fail $accessToken, $userId")
            _editCategoryItemNameState.value = LoadState.FAIL
            return
        }

        _editCategoryItemNameState.value = LoadState.START
        val request = EditCategoryNameRequest(userId, category.id, categoryName)

        viewModelScope.launch {
            runCatching {
                editCategoryItemNameUseCase.invoke(accessToken, request).apply {
                    Log.e("mybury", "edit response : $this")
                    when (this.retcode) {
                        "200" -> _editCategoryItemNameState.value = LoadState.SUCCESS
                        else -> _editCategoryItemNameState.value = LoadState.FAIL
                    }
                }
            }.getOrElse {
                Log.e("myvbury", "edit fail ${it.message}")
                _editCategoryItemNameState.value = LoadState.FAIL
            }
        }
    }

    fun changeCategoryStatus(list: List<Category>) {
        if (accessToken == null || userId == null) {
            _changeCategoryItemState.value = LoadState.FAIL
            return
        }

        val idList = list.map { it.id }
        val request = ChangeCategoryStatusRequest(userId, idList)
        _changeCategoryItemState.value = LoadState.START

        viewModelScope.launch {
            runCatching {
                changeCategoryListUseCase.invoke(accessToken, request).apply {
                    when (this.retcode) {
                        "200" -> _changeCategoryItemState.value = LoadState.SUCCESS
                        "301" -> getRefreshToken { result ->
                            _changeCategoryItemState.value = result
                        }
                        else -> _changeCategoryItemState.value = LoadState.FAIL
                    }
                }
            }.getOrElse {
                _changeCategoryItemState.value = LoadState.FAIL
            }
        }
    }
}