package womenproject.com.mybury.viewmodels

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import womenproject.com.mybury.base.BaseViewModel
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.network.OkHttp3RetrofitManager
import womenproject.com.mybury.network.RetrofitInterface

/**
 * Created by HanAYeon on 2019. 4. 23..
 */

class MyPageViewModel : BaseViewModel() {

    var nickname = "닉네임이 이렇게 길다리다링루룽"
    var doingBucketCount = "27"
    var doneBucketCount = "31"
    var ddayCount = "20"

    private val BUCKETLIST_API = "http://10.1.101.161/host/"

    fun getCategoryList(callback: BucketWriteViewModel.GetBucketListCallBackListener) {

        callback.start()

        val restClient: RetrofitInterface = OkHttp3RetrofitManager(BUCKETLIST_API).getRetrofitService(RetrofitInterface::class.java)

        val bucketListResultData = restClient.requestCategoryList()
        bucketListResultData.enqueue(object : Callback<BucketCategory> {
            override fun onResponse(call: Call<BucketCategory>, response: Response<BucketCategory>) {

                if (response != null && response.isSuccessful) {
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