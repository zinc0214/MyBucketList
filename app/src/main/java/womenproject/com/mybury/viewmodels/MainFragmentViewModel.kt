package womenproject.com.mybury.viewmodels

import womenproject.com.mybury.R
import womenproject.com.mybury.data.AdultCheck
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.network.OkHttp3RetrofitManager
import womenproject.com.mybury.network.RetrofitInterface

/**
 * Created by HanAYeon on 2018. 11. 28..
 */
class MainFragmentViewModel internal constructor(private val context: Context) : BaseViewModel() {

    private val MAIN_BUCKETLIST_API = context!!.resources.getString(R.string.main_bucketlist_api)

    private fun getMainBucketList(): BucketList {

        val restClient: RetrofitInterface = OkHttp3RetrofitManager(MAIN_BUCKETLIST_API).getRetrofitService(RetrofitInterface::class.java)

        val adultResultData = restClient.requestMainBucketListResult()
        adultResultData.enqueue(object : Callback<AdultCheck> {
            override fun onResponse(call: Call<AdultCheck>?, response: Response<AdultCheck>?) {
                if (response != null && response.isSuccessful) {
                    adultResult = response.body()!!
                    Log.e("ayhan:result", "${response.body()}")
                } else {
                    adultResult = null
                }
                checkFinish()
            }

            override fun onFailure(call: Call<AdultCheck>?, t: Throwable?) {
                Log.e("ayhan2", t.toString())
                errorReason = t.toString()
                adultResult = null
                checkFinish()
            }
        })

        return adultResult

    }

    fun getMainBucketList(): BucketList {

        /*
        * BucketType
        *   1 : 횟수1번
        *   2 : 횟수n번
        *   3 : 완료
        *   4 : 빈 버킷
        * DDay
        *   0 : dday 표기 안함
        *   1 : dday 표기함
        *   */

        val category = BucketCategory("id", "운동")
        val bucketItem1 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", false, false, category, 2, 5)
        val bucketItem2 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", false, false, category, 2, 5)
        val bucketItem3 = BucketItem("bucketlist02", "올림픽 공원에서 스케이트 타기", false, false, category, 2, 5)

        val bucketItemList = ArrayList<BucketItem>()

        bucketItemList.add(bucketItem1)
        bucketItemList.add(bucketItem2)
        bucketItemList.add(bucketItem3)

        val bucketList = BucketList(bucketItemList, false)

        return bucketList
    }
}