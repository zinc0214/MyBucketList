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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import org.json.JSONStringer
import java.util.concurrent.TimeUnit


/**
 * Created by HanAYeon on 2018. 12. 3..
 */

interface RetrofitInterface {

    @Headers("X-Naver-Client-Id: 3HRMQaekNO_olG_nNCHt", "X-Naver-Client-Secret: trOdm5SdqS")
    @GET("/v1/search/adult.json")
    fun requestAdultResult(@Query("query") query: String): Observable<AdultCheck>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/host/signup")
    fun postSignIn(@Body email: Email): Observable<ResponseBody>

    @GET("/host/home")
    fun requestMainBucketListResult(): Observable<BucketList>

    @GET("/host/home")
    fun requestMainBucketListResult(@Query("userId") userId: String, @Query("filter") filter: String, @Query("sort") sort: String): Observable<BucketList>


    @GET("/host/dDay")
    fun requestDdayBucketListResult(): Observable<BucketList>

    @GET("/host/beforeWrite")
    fun requestCategoryList(): Observable<BucketCategory>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/host/write")
    fun postAddBucketList(@Body params: AddBucketItem): Observable<ResponseBody>

    @Multipart
    @POST("/host/imageUpload")
    fun postAddBucketImage(@Part file : MultipartBody.Part): Observable<ResponseBody>

}

internal object APIClient {

    private var retrofit: Retrofit? = null

    val client: Retrofit
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            retrofit = Retrofit.Builder()
                    .baseUrl("http://54.92.251.44/host/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build()

            return retrofit as Retrofit
        }
}

val bucketListApi = Retrofit.Builder()
        .baseUrl("http://54.92.251.44/host/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(RetrofitInterface::class.java)
