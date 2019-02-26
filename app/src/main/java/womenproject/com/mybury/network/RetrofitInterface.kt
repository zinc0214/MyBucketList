package womenproject.com.mybury.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import womenproject.com.mybury.data.AdultCheck
import womenproject.com.mybury.data.BucketList

/**
 * Created by HanAYeon on 2018. 12. 3..
 */

interface RetrofitInterface {

    @Headers("X-Naver-Client-Id: 3HRMQaekNO_olG_nNCHt", "X-Naver-Client-Secret: trOdm5SdqS")
    @GET("/v1/search/adult.json")
    fun requestAdultResult(@Query("query") query: String): Call<AdultCheck>

    @GET("/host/home")
    fun requestMainBucketListResult() : Call<BucketList>

    @GET("/host/dDay")
    fun requestDdayBucketListResult() : Call<BucketList>

}