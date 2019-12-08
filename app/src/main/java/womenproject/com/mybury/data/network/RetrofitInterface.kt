package womenproject.com.mybury.data.network

import io.reactivex.Observable
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import womenproject.com.mybury.data.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Headers


/**
 * Created by HanAYeon on 2018. 12. 3..
 */

interface RetrofitInterface {

    @Headers("X-Naver-Client-Id: 3HRMQaekNO_olG_nNCHt", "X-Naver-Client-Secret: trOdm5SdqS")
    @GET("/v1/search/adult.json")
    fun requestAdultResult(@Query("query") query: String): Observable<AdultCheck>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/host/signup_check")
    fun postSignUpCheck(@Body email: SignUpCheckRequest): Observable<SignUpCheckResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/host/signup")
    fun postSignUp(@Body email: SignUpCheckRequest): Observable<SignUpResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/host/signin")
    fun getLoginToken(@Body email: GetTokenRequest): Observable<GetTokenResponse>

    @POST("/host/profile")
    @Multipart
    fun postCreateProfile(@Header("X-Auth-Token") token: String,
                          @Part userId: MultipartBody.Part,
                          @Part name: MultipartBody.Part,
                          @Part file: MultipartBody.Part): Observable<SimpleResponse>

    @GET("/host/home")
    fun requestHomeBucketList(@Header("X-Auth-Token") token: String, @Query("userId") userId: String, @Query("filter") filter: String, @Query("sort") sort: String): Observable<BucketList>

    @GET("/host/home")
    fun requestHomeBucketList(): Observable<BucketList>

    @GET("/host/dDay")
    fun requestDdayBucketListResult(): Observable<BucketList>

    @GET("/host/beforeWrite")
    fun requestCategoryList(): Observable<BucketCategory>

    @GET("/host/beforeWrite")
    fun requestBeforeWrite(@Query("userId") userId: String) : Observable<ResponseBody>

    @POST("/host/write")
    @Multipart
    fun postAddBucketList(@Header("X-Auth-Token") token: String,
                          @Part title: MultipartBody.Part,
                          @Part open: MultipartBody.Part,
                          @Part dDate: MultipartBody.Part,
                          @Part goalCount: MultipartBody.Part,
                          @Part memo: MultipartBody.Part,
                          @Part categoryId: MultipartBody.Part,
                          @Part userId: MultipartBody.Part): Observable<SimpleResponse>

    @POST("/host/write")
    @Multipart
    fun postAddBucketList(@Header("X-Auth-Token") token: String,
                          @Part bucketItem: MultipartBody.Part,
                          @Part userId: MultipartBody.Part): Observable<SimpleResponse>

    @Multipart
    @POST("/host/imageUpload")
    fun postAddBucketImage(@Part file: MultipartBody.Part): Observable<ResponseBody>

}

internal object APIClient {

    private var retrofit: Retrofit? = null

    val client: Retrofit
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            retrofit = Retrofit.Builder()
                    .baseUrl("http://3.92.189.69/host/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build()

            return retrofit as Retrofit
        }
}

/*
val bucketListApi = Retrofit.Builder()
        .baseUrl("http://3.92.189.69/host/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(RetrofitInterface::class.java)
*/

val apiInterface = APIClient.client.create(RetrofitInterface::class.java)