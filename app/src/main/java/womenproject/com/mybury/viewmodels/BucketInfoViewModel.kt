package womenproject.com.mybury.viewmodels

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import womenproject.com.mybury.base.BaseViewModel
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.network.OkHttp3RetrofitManager
import womenproject.com.mybury.network.RetrofitInterface

/**
 * Created by HanAYeon on 2019-06-25.
 */

class BucketInfoViewModel : BaseViewModel() {


    interface GetBucketListCallBackListener {
        fun start()
        fun success(bucketCategory : BucketCategory)
        fun fail()
    }

    interface OnBucketListGetEvent {
        fun start()
        fun finish(bucketList: BucketList?)
    }


    private var bucketList : BucketList? = null

    companion object {
        private const val BUCKETLIST_API = "http://10.1.101.161/host/"
    }



    fun getMainBucketList(api: String, callback: OnBucketListGetEvent): BucketList? {

        callback.start()

        val restClient: RetrofitInterface = OkHttp3RetrofitManager(api).getRetrofitService(RetrofitInterface::class.java)

        val bucketListResultData = restClient.requestMainBucketListResult()
        bucketListResultData.enqueue(object : Callback<BucketList> {
            override fun onResponse(call: Call<BucketList>?, response: Response<BucketList>?) {

                if (response != null && response.isSuccessful) {
                    Log.e("ayhan:result", "${response.body()}")
                    bucketList = response.body()
                    Log.e("ayhan:bucketList", "$bucketList")
                    callback.finish(bucketList)
                }
            }

            override fun onFailure(call: Call<BucketList>?, t: Throwable?) {
                Log.e("ayhan2", t.toString())
                //progressVisible.set(View.GONE)
                callback.finish(bucketList)
            }
        })

        return bucketList

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