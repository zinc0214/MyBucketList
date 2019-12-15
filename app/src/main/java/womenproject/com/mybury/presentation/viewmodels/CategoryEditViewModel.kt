package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.CanNotGoMainDialog

/**
 * Created by HanAYeon on 2019-08-19.
 */

class CategoryInfoViewModel {


    interface ChangeCategoryState{
        fun start()
        fun success()
        fun fail()
    }

    fun removeCategoryItem(category: HashSet<String>, changeCategoryState: ChangeCategoryState) {
        // 카테고리 제거
    }

    @SuppressLint("CheckResult")
    fun addCategoryItem(tokenId : String, categoryName : String, changeCategoryState: ChangeCategoryState) {
        /*changeCategoryState.start()
        apiInterface.addNewCategoryItem(tokenId, categoryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    changeCategoryState.success()
                }) {
                   changeCategoryState.fail()
                }*/

        changeCategoryState.start()
        changeCategoryState.success()
    }

    @SuppressLint("CheckResult")
    fun editCategoryItem(tokenId : String, categoryName : String, changeCategoryState: ChangeCategoryState) {
        /*changeCategoryState.start()
        apiInterface.addNewCategoryItem(tokenId, categoryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    changeCategoryState.success()
                }) {
                   changeCategoryState.fail()
                }*/

        changeCategoryState.start()
        changeCategoryState.success()
    }


}