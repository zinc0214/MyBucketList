package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import womanproject.com.mybury.domain.usecase.category.AddCategoryItemUseCase
import womanproject.com.mybury.domain.usecase.category.LoadCategoryListUseCase
import womenproject.com.mybury.data.*
import womenproject.com.mybury.data.model.AddCategoryRequest
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val loadCategoryListUseCase: LoadCategoryListUseCase,
    private val addCategoryItemUseCase: AddCategoryItemUseCase
) : BaseViewModel() {

    private val _categoryLoadState = MutableLiveData<LoadState>()
    val categoryLoadState: LiveData<LoadState> = _categoryLoadState

    private val _categoryList = MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>> = _categoryList

    private val _addCategoryItemState = MutableLiveData<LoadState>()
    val addCategoryItemState: LiveData<LoadState> = _addCategoryItemState

    fun loadCategoryList() {
        if (accessToken == null || userId == null) {
            _categoryLoadState.value = LoadState.START
            return
        }

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

    @SuppressLint("CheckResult")
    fun removeCategoryItem(categoryId: HashSet<String>, callBack: Simple3CallBack) {
        if (accessToken == null || userId == null) {
            callBack.fail()
            return
        }

        // 카테고리 제거
        val list = ArrayList<String>()
        for (i in categoryId) {
            list.add(i)
        }
        val request = RemoveCategoryRequest(userId, list)
        apiInterface.removeCategoryItem(accessToken, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when {
                    it.retcode == "200" -> callBack.success()
                    it.retcode == "301" -> getRefreshToken(object : SimpleCallBack {
                        override fun success() {
                            callBack.restart()
                        }

                        override fun fail() {
                            callBack.fail()
                        }
                    })
                    else -> callBack.fail()
                }
            }) {
                callBack.fail()
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
                        else -> _addCategoryItemState.value = LoadState.RESTART
                    }
                }
            }.getOrElse {
                _addCategoryItemState.value = LoadState.RESTART
            }
        }
    }

    @SuppressLint("CheckResult")
    fun editCategoryItem(category: Category, categoryName: String, callBack: Simple3CallBack) {
        if (accessToken == null || userId == null) {
            callBack.fail()
            return
        }

        callBack.start()

        val request = EditCategoryNameRequest(userId, category.id, categoryName)
        apiInterface.editCategoryItemName(accessToken, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callBack.success()
            }) {
                callBack.fail()
            }
    }

    @SuppressLint("CheckResult")
    fun changeCategoryStatus(list: List<Category>, callBack: Simple3CallBack) {
        if (accessToken == null || userId == null) {
            callBack.fail()
            return
        }

        var idList = arrayListOf<String>()
        for (i in list) {
            idList.add(i.id)
        }
        val request = ChangeCategoryStatusRequest(userId, idList)
        callBack.start()
        apiInterface.changeCategoryList(accessToken, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it.retcode) {
                    "200" -> callBack.success()
                    "301" -> getRefreshToken(object : SimpleCallBack {
                        override fun success() {
                            callBack.restart()
                        }

                        override fun fail() {
                            callBack.fail()
                        }
                    })
                    else -> callBack.fail()
                }
            }) {
                callBack.fail()
            }
    }

}