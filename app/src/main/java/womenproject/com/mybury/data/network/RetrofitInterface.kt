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
    fun getLoginToken(@Body email: UseUserIdRequest): Observable<GetTokenResponse>

    @POST("/host/profile")
    @Multipart
    fun postCreateProfile(@Header("X-Auth-Token") token: String,
                          @Part userId: MultipartBody.Part,
                          @Part name: MultipartBody.Part,
                          @Part file: MultipartBody.Part): Observable<SimpleResponse>

    @POST("/host/profile")
    @Multipart
    fun postCreateProfile(@Header("X-Auth-Token") token: String,
                          @Part userId: MultipartBody.Part,
                          @Part name: MultipartBody.Part): Observable<SimpleResponse>


    @GET("/host/home")
    fun requestHomeBucketList(@Header("X-Auth-Token") token: String, @Query("userId") userId: String, @Query("filter") filter: String, @Query("sort") sort: String): Observable<BucketList>

    @GET("/host/category")
    fun requestCategoryBucketList(@Header("X-Auth-Token") token: String, @Query("categoryId") userId: String): Observable<BucketList>

    @GET("/host/bucketlist/{bucketId}")
    fun requestDetailBucketList(@Header("X-Auth-Token") token: String, @Path("bucketId") bucketId : String): Observable<DetailBucketItem>

    @POST("/host/complete")
    fun postCompleteBucket(@Header("X-Auth-Token") token: String,
                           @Body bucketRequest: BucketRequest): Observable<SimpleResponse>


    @GET("/host/dDay")
    fun requestDdayBucketListResult(@Header("X-Auth-Token") token: String, @Query("userId") userId: String): Observable<DdayBucketListRespone>

    @GET("/host/beforeWrite")
    fun requestBeforeWrite(@Header("X-Auth-Token") token: String, @Query("userId") userId: String): Observable<BucketCategory>

    @POST("/host/write")
    @Multipart
    fun postAddBucketList(@Header("X-Auth-Token") token: String,
                          @Part title: MultipartBody.Part,
                          @Part open: MultipartBody.Part,
                          @Part dDate: MultipartBody.Part,
                          @Part goalCount: MultipartBody.Part,
                          @Part memo: MultipartBody.Part,
                          @Part categoryId: MultipartBody.Part,
                          @Part userId: MultipartBody.Part,
                          @Part multipartFiles: List<MultipartBody.Part>): Observable<SimpleResponse>

    @POST("/host/write")
    @Multipart
    fun postAddBucketListNotDDay(@Header("X-Auth-Token") token: String,
                                 @Part title: MultipartBody.Part,
                                 @Part open: MultipartBody.Part,
                                 @Part goalCount: MultipartBody.Part,
                                 @Part memo: MultipartBody.Part,
                                 @Part categoryId: MultipartBody.Part,
                                 @Part userId: MultipartBody.Part,
                                 @Part multipartFiles: List<MultipartBody.Part>): Observable<SimpleResponse>


    @POST("/host/bucketlist/{bucketId}")
    @Multipart
    fun postUpdateBucketList(@Header("X-Auth-Token") token: String,
                             @Path("bucketId") bucketId : String,
                          @Part title: MultipartBody.Part,
                          @Part open: MultipartBody.Part,
                          @Part dDate: MultipartBody.Part,
                          @Part goalCount: MultipartBody.Part,
                          @Part memo: MultipartBody.Part,
                          @Part categoryId: MultipartBody.Part,
                          @Part userId: MultipartBody.Part,
                          @Part multipartFiles: List<MultipartBody.Part>): Observable<SimpleResponse>

    @POST("/host/bucketlist/{bucketId}")
    @Multipart
    fun postUpdateBucketListNotDDay(@Header("X-Auth-Token") token: String,
                                    @Path("bucketId") bucketId : String,
                                 @Part title: MultipartBody.Part,
                                 @Part open: MultipartBody.Part,
                                 @Part goalCount: MultipartBody.Part,
                                 @Part memo: MultipartBody.Part,
                                 @Part categoryId: MultipartBody.Part,
                                 @Part userId: MultipartBody.Part,
                                 @Part multipartFiles: List<MultipartBody.Part>): Observable<SimpleResponse>


    @POST("/host/category")
    fun addNewCategoryItem(@Header("X-Auth-Token") token: String,
                           @Body categoryId: String): Observable<SimpleResponse>

    @GET("/host/mypage")
    fun loadMyPageData(@Header("X-Auth-Token") token: String, @Query("userId") userId: String): Observable<MyPageInfo>

    @GET("/host/terms_of_use")
    fun loadTermsOfUse(@Header("X-Auth-Token") token: String): Observable<ResponseBody>

    @GET("/host/privacy_policy")
    fun loadPrivacyPolicy(@Header("X-Auth-Token") token: String): Observable<ResponseBody>


    @HTTP(method = "DELETE", path = "/host/withdrawal", hasBody = true)
    fun postSignOut(@Body userId: UseUserIdRequest): Observable<SimpleResponse>

}

internal object APIClient {

    private var retrofit: Retrofit? = null

    val client: Retrofit
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            retrofit = Retrofit.Builder()
                    .baseUrl("http://3.92.189.69")
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