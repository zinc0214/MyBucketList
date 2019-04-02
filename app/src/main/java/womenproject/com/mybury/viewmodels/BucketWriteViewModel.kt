package womenproject.com.mybury.viewmodels

import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import womenproject.com.mybury.base.BaseViewModel
import womenproject.com.mybury.data.AddBucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.BucketUserCategory
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

    fun getCategoryList(): BucketUserCategory {

        val cat1 = "여행"
        val cat2 = "음식"

        val category = ArrayList<String>()

        category.add(cat1)
        category.add(cat2)

        return BucketUserCategory(category)

    }

    fun addBucketList(bucketItem: AddBucketItem, callback: OnBucketAddEvent) {

        callback.start()

        val restClient: RetrofitInterface = OkHttp3RetrofitManager(BUCKETLIST_API).getRetrofitService(RetrofitInterface::class.java)

        val bucketListResultData = restClient.postAddBucketList(setAddBucketParams(bucketItem))
        bucketListResultData.enqueue(object : Callback<AddBucketItem> {
            override fun onResponse(call: Call<AddBucketItem>?, response: Response<AddBucketItem>?) {

                if (response != null && response.isSuccessful) {
                    Log.e("ayhan:result", "${response.body()}")
                    callback.success()
                }
            }

            override fun onFailure(call: Call<AddBucketItem>?, t: Throwable?) {
                Log.e("ayhan2", t.toString())
                callback.fail()
            }
        })

    }


    private fun setAddBucketParams(bucketItem: AddBucketItem) : HashMap<String, String> {
        var paramsHaspMap = HashMap<String, String>()

        paramsHaspMap.put("title", bucketItem.title)
        paramsHaspMap.put("memo", bucketItem.memo)
        paramsHaspMap.put("img", bucketItem.img.toString())
        paramsHaspMap.put("category", bucketItem.category)
        paramsHaspMap.put("dday", bucketItem.dday)


        return paramsHaspMap
    }
}