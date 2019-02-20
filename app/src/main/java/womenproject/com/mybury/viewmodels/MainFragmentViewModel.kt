package womenproject.com.mybury.viewmodels

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.network.OkHttp3RetrofitManager
import womenproject.com.mybury.network.RetrofitInterface

/**
 * Created by HanAYeon on 2018. 11. 28..
 */
class MainFragmentViewModel internal constructor(private val context: Context?) : BaseViewModel() {

    private val MAIN_BUCKETLIST_API = context!!.resources.getString(R.string.bucket_list_api)
    private var bucketList : BucketList? = null
    var progressVisible = ObservableInt(View.GONE)


    interface OnBucketListGetEvent {
        fun start()
        fun finish(bucketList: BucketList?)
    }

    fun getMainBucketList(callback: OnBucketListGetEvent): BucketList? {

        callback.start()

        val restClient: RetrofitInterface = OkHttp3RetrofitManager(MAIN_BUCKETLIST_API).getRetrofitService(RetrofitInterface::class.java)

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