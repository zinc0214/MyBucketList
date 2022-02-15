package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.*
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by HanAYeon on 2019-08-19.
 */

@HiltViewModel
class CategoryEditViewModel @Inject constructor(): BaseViewModel() {

    @SuppressLint("CheckResult")
    fun removeCategoryItem(categoryId: HashSet<String>, callBack: Simple3CallBack) {
        if(accessToken==null || userId == null) {
            callBack.fail()
            return
        }

        // 카테고리 제거
        val list = ArrayList<String>()
        for(i in categoryId) {
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

    @SuppressLint("CheckResult")
    fun addCategoryItem(categoryName: String, callBack: Simple3CallBack) {
        if(accessToken==null || userId == null) {
            callBack.fail()
            return
        }

        callBack.start()
        val request = AddCategoryRequest(userId, categoryName)
        apiInterface.addNewCategoryItem(accessToken, request)
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

    @SuppressLint("CheckResult")
    fun editCategoryItem(category : Category, categoryName: String, callBack: Simple3CallBack) {
        if(accessToken==null || userId == null) {
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
        if(accessToken==null || userId == null) {
            callBack.fail()
            return
        }

        var idList = arrayListOf<String>()
        for(i in list) {
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