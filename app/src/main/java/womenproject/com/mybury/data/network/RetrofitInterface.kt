package womenproject.com.mybury.data.network

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import womenproject.com.mybury.data.*


/**
 * Created by HanAYeon on 2018. 12. 3..
 */

interface RetrofitInterface {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/signin")
    fun getLoginToken(@Body email: UseUserIdRequest): Observable<GetTokenResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/refresh_token")
    fun getRefershToken(@Body refreshToken: NewTokenRequest): Observable<GetTokenResponse>

    // 프로필 이미지가 있는 경우
    @POST("/profile")
    @Multipart
    fun postCreateProfile(
        @Header("X-Auth-Token") token: String,
        @Part userId: MultipartBody.Part,
        @Part name: MultipartBody.Part,
        @Part file: MultipartBody.Part,
        @Part defaultImg: MultipartBody.Part
    ): Observable<SimpleResponse>

    // 프로필 이미지가 없는 경우
    @POST("/profile")
    @Multipart
    fun postCreateProfile(
        @Header("X-Auth-Token") token: String,
        @Part userId: MultipartBody.Part,
        @Part name: MultipartBody.Part,
        @Part defaultImg: MultipartBody.Part
    ): Observable<SimpleResponse>


    @GET("/home")
    fun requestHomeBucketList(
        @Header("X-Auth-Token") token: String,
        @Query("userId") userId: String,
        @Query("filter") filter: String,
        @Query("sort") sort: String
    ): Observable<BucketList>

    @GET("/category")
    fun requestCategoryBucketList(
        @Header("X-Auth-Token") token: String,
        @Query("categoryId") categoryId: String
    ): Observable<BucketList>

    @GET("/bucketlist/{bucketId}")
    fun requestDetailBucketList(
        @Header("X-Auth-Token") token: String,
        @Path("bucketId") bucketId: String,
        @Query("userId") userId: String
    ): Observable<DetailBucketItem>

    @POST("/complete")
    fun postCompleteBucket(
        @Header("X-Auth-Token") token: String,
        @Body bucketRequest: BucketRequest
    ): Observable<SimpleResponse>

    @POST("/redo")
    suspend fun postRedoBucket(
        @Header("X-Auth-Token") token: String,
        @Body bucketRequest: StatusChangeBucketRequest
    ): SimpleResponse


    @GET("/dDay")
    fun requestDdayBucketListResult(
        @Header("X-Auth-Token") token: String,
        @Query("userId") userId: String,
        @Query("filter") filter: String
    ): Observable<DdayBucketListRespone>

    @POST("/write")
    @Multipart
    fun postAddBucketList(
        @Header("X-Auth-Token") token: String,
        @Part title: MultipartBody.Part,
        @Part open: MultipartBody.Part,
        @Part dDate: MultipartBody.Part? = null,
        @Part goalCount: MultipartBody.Part,
        @Part memo: MultipartBody.Part,
        @Part categoryId: MultipartBody.Part,
        @Part userId: MultipartBody.Part,
        @Part image1: MultipartBody.Part? = null,
        @Part image2: MultipartBody.Part? = null,
        @Part image3: MultipartBody.Part? = null
    ): Observable<SimpleResponse>


    @POST("/bucketlist/{bucketId}")
    @Multipart
    fun postUpdateBucketList(
        @Header("X-Auth-Token") token: String,
        @Path("bucketId") bucketId: String,
        @Part title: MultipartBody.Part,
        @Part open: MultipartBody.Part,
        @Part dDate: MultipartBody.Part? = null,
        @Part goalCount: MultipartBody.Part,
        @Part memo: MultipartBody.Part,
        @Part categoryId: MultipartBody.Part,
        @Part userId: MultipartBody.Part,
        @Part image1: MultipartBody.Part? = null,
        @Part noImg1: MultipartBody.Part,
        @Part image2: MultipartBody.Part? = null,
        @Part noImg2: MultipartBody.Part,
        @Part image3: MultipartBody.Part? = null,
        @Part noImg3: MultipartBody.Part
    ): Observable<SimpleResponse>


    @HTTP(method = "DELETE", path = "/bucketlist/{bucketId}", hasBody = true)
    suspend fun deleteBucket(
        @Header("X-Auth-Token") token: String,
        @Body userId: UseUserIdRequest,
        @Path("bucketId") bucketId: String
    ): SimpleResponse

    @POST("/category")
    fun addNewCategoryItem(
        @Header("X-Auth-Token") token: String,
        @Body categoryId: AddCategoryRequest
    ): Observable<SimpleResponse>

    @POST("/category/edit_name")
    fun editCategoryItemName(
        @Header("X-Auth-Token") token: String,
        @Body categoryId: EditCategoryNameRequest
    ): Observable<SimpleResponse>

    @POST("/category/edit_priority")
    fun changeCategoryList(
        @Header("X-Auth-Token") token: String,
        @Body categoryId: ChangeCategoryStatusRequest
    ): Observable<SimpleResponse>


    @HTTP(method = "DELETE", path = "/category", hasBody = true)
    fun removeCategoryItem(
        @Header("X-Auth-Token") token: String,
        @Body categoryId: RemoveCategoryRequest
    ): Observable<SimpleResponse>

    @GET("/mypage")
    fun loadMyPageData(
        @Header("X-Auth-Token") token: String,
        @Query("userId") userId: String
    ): Observable<MyPageInfo>

    @HTTP(method = "DELETE", path = "/withdrawal", hasBody = true)
    fun postSignOut(
        @Header("X-Auth-Token") token: String,
        @Body userId: UseUserIdRequest
    ): Observable<SimpleResponse>

    // 후원 아이템 리스트
    @POST("/support_items")
    suspend fun getSupportItem(
        @Header("X-Auth-Token") token: String,
        @Body userId: UseUserIdRequest
    ): SupportInfo

    // 후원하기
    @POST("/support")
    suspend fun requestSupportItem(
        @Header("X-Auth-Token") token: String,
        @Body purchasedItem: PurchasedItem
    ): SimpleResponse

    // 후원하기 성공 또는 실패 여부 전달
    @POST("/support_edit")
    suspend fun editSupportResult(
        @Header("X-Auth-Token") token: String,
        @Body purchasedResult: PurchasedResult
    ): SimpleResponse

    @GET("/home")
    suspend fun requestSortBucketList(
        @Header("X-Auth-Token") token: String,
        @Query("userId") userId: String,
        @Query("filter") filter: String,
        @Query("sort") sort: String
    ): BucketList

    @POST("/change_order")
    suspend fun updateBucketListOrder(
        @Header("X-Auth-Token") token: String,
        @Body bucketListOrder: BucketListOrder
    ): SimpleResponse

    @POST("/search")
    suspend fun searchList(
        @Header("X-Auth-Token") token: String,
        @Body searchRequest: SearchRequest
    ): SearchResult
}

internal object APIClient {

    private var retrofit: Retrofit? = null

    val client: Retrofit
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            retrofit = Retrofit.Builder()
                .baseUrl("https://www.my-bury.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()

            return retrofit as Retrofit
        }
}

val apiInterface: RetrofitInterface = APIClient.client.create(RetrofitInterface::class.java)