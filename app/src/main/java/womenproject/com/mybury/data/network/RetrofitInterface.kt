package womenproject.com.mybury.data.network

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import womenproject.com.mybury.data.*
import java.io.File

/**
 * Created by HanAYeon on 2018. 12. 3..
 */

interface RetrofitInterface {

    @Headers("X-Naver-Client-Id: 3HRMQaekNO_olG_nNCHt", "X-Naver-Client-Secret: trOdm5SdqS")
    @GET("/v1/search/adult.json")
    fun requestAdultResult(@Query("query") query: String): Observable<AdultCheck>

    @GET("/host/home")
    fun requestMainBucketListResult(): Observable<BucketList>

    @GET("/host/dDay")
    fun requestDdayBucketListResult(): Observable<BucketList>

    @GET("/host/beforeWrite")
    fun requestCategoryList(): Observable<BucketCategory>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("/host/write")
    fun postAddBucketList(@Body params: AddBucketItem): Observable<ResponseBody>

    @Multipart
    @POST("/host/imageUpload")
    fun postAddBucketImage(@Part file : MultipartBody.Part): Observable<ResponseBody>

}


val bucketListApi = Retrofit.Builder()
        .baseUrl("http://10.1.101.161/host/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(RetrofitInterface::class.java)
