package womenproject.com.mybury.data.api

import okhttp3.MultipartBody
import retrofit2.http.*
import womenproject.com.mybury.data.model.*

interface MyBuryApi {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/refresh_token")
    suspend fun getRefreshToken(@Body refreshToken: RefreshTokenRequest): RefreshTokenResponse

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/signup_check")
    suspend fun signUpCheck(@Body email: SignUpCheckRequest): SignUpCheckResponse

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/signup")
    suspend fun signUp(@Body email: SignUpCheckRequest): SignUpResponse

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/signin")
    suspend fun getLoginToken(@Body email: UseUserIdRequest): GetTokenResponse

    @GET("/home")
    suspend fun loadHomeBucketList(
        @Header("X-Auth-Token") token: String,
        @Query("userId") userId: String,
        @Query("filter") filter: String,
        @Query("sort") sort: String
    ): DomainBucketList

    @POST("/cancel")
    suspend fun cancelBucketItem(
        @Header("X-Auth-Token") token: String,
        @Body bucketRequest: StatusChangeBucketRequest
    ): SimpleResponse

    @GET("/beforeWrite")
    suspend fun loadCategoryList(
        @Header("X-Auth-Token") token: String,
        @Query("userId") userId: String
    ): BucketCategory

    @GET("/category")
    suspend fun loadBucketListByCategory(
        @Header("X-Auth-Token") token: String,
        @Query("categoryId") categoryId: String
    ): DomainBucketList

    @GET("/bucketlist/{bucketId}")
    suspend fun loadDetailBucketInfo(
        @Header("X-Auth-Token") token: String,
        @Path("bucketId") bucketId: String,
        @Query("userId") userId: String
    ): BucketDetailItem

    @HTTP(method = "DELETE", path = "/bucketlist/{bucketId}", hasBody = true)
    suspend fun deleteBucket(
        @Header("X-Auth-Token") token: String,
        @Body userId: UserIdRequest,
        @Path("bucketId") bucketId: String
    ): SimpleResponse

    @POST("/complete")
    suspend fun completeBucket(
        @Header("X-Auth-Token") token: String,
        @Body bucketRequest: BucketRequest
    ): SimpleResponse

    @POST("/profile")
    @Multipart
    suspend fun updateProfile(
        @Header("X-Auth-Token") token: String,
        @Part userId: MultipartBody.Part,
        @Part name: MultipartBody.Part,
        @Part defaultImg: MultipartBody.Part,
        @Part multipartFile: MultipartBody.Part? = null
    ): SimpleResponse

    @POST("/redo")
    suspend fun redoBucket(
        @Header("X-Auth-Token") token: String,
        @Body bucketRequest: StatusChangeBucketRequest
    ): SimpleResponse

    @GET("/dDay")
    suspend fun loadDdayBucketList(
        @Header("X-Auth-Token") token: String,
        @Query("userId") userId: String,
        @Query("filter") filter: String
    ): DdayBucketListResponse

    @POST("/write")
    @Multipart
    suspend fun addBucketItem(
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
    ): SimpleResponse

    @POST("/bucketlist/{bucketId}")
    @Multipart
    suspend fun updateBucketList(
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
    ): SimpleResponse

    @POST("/category")
    suspend fun addNewCategoryItem(
        @Header("X-Auth-Token") token: String,
        @Body categoryId: AddCategoryRequest
    ): SimpleResponse

    @POST("/category/edit_name")
    suspend fun editCategoryItemName(
        @Header("X-Auth-Token") token: String,
        @Body categoryId: EditCategoryNameRequest
    ): SimpleResponse

    @POST("/category/edit_priority")
    suspend fun changeCategoryList(
        @Header("X-Auth-Token") token: String,
        @Body categoryId: ChangeCategoryStatusRequest
    ): SimpleResponse

    @HTTP(method = "DELETE", path = "/category", hasBody = true)
    suspend fun removeCategoryItem(
        @Header("X-Auth-Token") token: String,
        @Body removeCategoryRequest: RemoveCategoryRequest
    ): SimpleResponse

    @GET("/mypage")
    suspend fun loadMyPageInfo(
        @Header("X-Auth-Token") token: String,
        @Query("userId") userId: String
    ): OriginMyPageInfo

}
