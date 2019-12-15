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
import womenproject.com.mybury.presentation.base.BaseViewModel

/**
 * Created by HanAYeon on 2019-08-19.
 */

class CategoryInfoViewModel : BaseViewModel() {


    fun removeCategoryItem(category: HashSet<String>, changeCategoryState:Simple3CallBack) {
        // 카테고리 제거
    }

    @SuppressLint("CheckResult")
    fun addCategoryItem(categoryName : String, changeCategoryState: Simple3CallBack) {
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
    fun editCategoryItem(categoryName : String, changeCategoryState: Simple3CallBack) {
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