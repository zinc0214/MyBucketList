package womenproject.com.mybury.network

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*
import womenproject.com.mybury.data.*

/**
 * Created by HanAYeon on 2018. 12. 3..
 */

interface RetrofitInterface {

    @Headers("X-Naver-Client-Id: 3HRMQaekNO_olG_nNCHt", "X-Naver-Client-Secret: trOdm5SdqS")
    @GET("/v1/search/adult.json")
    fun requestAdultResult(@Query("query") query: String): Call<AdultCheck>

    @GET("/host/home")
    fun requestMainBucketListResult(): Call<BucketList>

    @GET("/host/dDay")
    fun requestDdayBucketListResult(): Call<BucketList>

    @GET("/host/beforeWrite")
    fun requestCategoryList(): Call<BucketCategory>


    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("/host/write")
    fun postAddBucketList(@Body params: AddBucketItem): Call<ResponseBody>

    @Multipart
    @POST("/host/write")
    fun postAddBucketImage(@FieldMap params: Array<String>): Call<AddBucketItem>

}