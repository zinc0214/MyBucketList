package womenproject.com.mybury.presentation.viewmodels

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.network.OkHttp3RetrofitManager
import womenproject.com.mybury.data.network.RetrofitInterface

class WriteCategoryDialogViewModel : BaseViewModel() {

    private var bucketList : BucketList? = null


    interface OnBucketListGetEvent {
        fun start()
        fun finish(bucketList: BucketList?)
    }

    fun getMainBucketList(api : String, callback: OnBucketListGetEvent): BucketList? {

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
}