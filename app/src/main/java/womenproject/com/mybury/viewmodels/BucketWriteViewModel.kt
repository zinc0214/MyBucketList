package womenproject.com.mybury.viewmodels

import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import womenproject.com.mybury.base.BaseViewModel
import womenproject.com.mybury.data.AddBucketItem
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.network.OkHttp3RetrofitManager
import womenproject.com.mybury.network.RetrofitInterface

class BucketWriteViewModel : BaseViewModel() {


    private val BUCKETLIST_API = "http://10.1.101.161/host/"

    private var bucketList: BucketList? = null
    var progressVisible = ObservableInt(View.GONE)


    interface OnBucketAddEvent {
        fun start()
        fun success()
        fun fail()
    }


    interface GetBucketListCallBackListener {
        fun start()
        fun success(bucketCategory : BucketCategory)
        fun fail()
    }

    fun getCategoryList(callback: GetBucketListCallBackListener) {

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

    fun addBucketList(bucketItem: AddBucketItem, callback: OnBucketAddEvent) {

        callback.start()

        val restClient: RetrofitInterface = OkHttp3RetrofitManager(BUCKETLIST_API).getRetrofitService(RetrofitInterface::class.java)

        val bucketListResultData = restClient.postAddBucketList(bucketItem)
        bucketListResultData.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response != null && response.isSuccessful) {
                    Log.e("ayhan:result_addBucketList", "${response.body()}")
                    callback.success()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("ayhan2_addBucketList", t.toString())
                callback.fail()
            }
        })

    }

}