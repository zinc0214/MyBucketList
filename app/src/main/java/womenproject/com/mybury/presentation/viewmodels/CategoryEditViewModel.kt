package womenproject.com.mybury.presentation.viewmodels

import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.Category

/**
 * Created by HanAYeon on 2019-08-19.
 */

class CategoryInfoViewModel {


    fun getDummyCategory() : BucketCategory{
        val categoryList = Category("없음", "1010101010", 1)
        val bucketCategory = BucketCategory(arrayListOf(categoryList), "200")
        return bucketCategory
    }


    interface ChangeCategoryState{
        fun start()
        fun success()
        fun fail()
    }

    fun removeCategoryItem(category: HashSet<String>, changeCategoryState: ChangeCategoryState) {
        // 카테고리 제거
    }

    fun addCategoryItem() {

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