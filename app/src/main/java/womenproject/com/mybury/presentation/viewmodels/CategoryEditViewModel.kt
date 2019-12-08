package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.CategoryList
import womenproject.com.mybury.data.network.apiInterface

/**
 * Created by HanAYeon on 2019-08-19.
 */

class CategoryInfoViewModel {


    interface GetBucketListCallBackListener {
        fun start()
        fun success(bucketCategory : BucketCategory)
        fun fail()
    }

    @SuppressLint("CheckResult")
    fun getCategoryList(callback: GetBucketListCallBackListener) {

        apiInterface.requestCategoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    callback.fail()
                    callback.success(getDummyCategory())
                }
                .subscribe({
                    bucketCategory -> callback.success(bucketCategory)
                }) {
                    Log.e("ayhan", it.toString())
                }
    }

    fun getDummyCategory() : BucketCategory{
        val categoryList = CategoryList("없음")
        val bucketCategory = BucketCategory(arrayListOf(categoryList), "200")
        return bucketCategory
    }

/*    fun getCategoryList2(callback: GetBucketListCallBackListener) {

        callback.start()

        val restClient: RetrofitInterface = OkHttp3RetrofitManager(BUCKETLIST_API).getRetrofitService(RetrofitInterface::class.java)

        val bucketListResultData = restClient.requestCategoryList()
        bucketListResultData.enqueue(object : Callback<BucketCategory> {
            override fun onResponse(call: Call<BucketCategory>, response: Response<BucketCategory>) {

                if (response.isSuccessful) {
                    Log.e("ayhan:result_addBucketList", "${response.body()}")
                    callback.success(response.body()!!)
                }
            }

            override fun onFailure(call: Call<BucketCategory>, t: Throwable) {
                Log.e("ayhan2_addBucketList", t.toString())
                callback.fail()
            }
        })



    }*/
}