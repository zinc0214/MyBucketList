package womenproject.com.mybury.presentation.viewmodels

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.network.OkHttp3RetrofitManager
import womenproject.com.mybury.data.network.RetrofitInterface

/**
 * Created by HanAYeon on 2019-08-19.
 */

class CategoryInfoViewModel {


    interface GetBucketListCallBackListener {
        fun start()
        fun success(bucketCategory : BucketCategory)
        fun fail()
    }


    companion object {
        private const val BUCKETLIST_API = "http://10.1.101.161/host/"
    }


    fun getCategoryList(callback: GetBucketListCallBackListener) {

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



    }
}