package womenproject.com.mybury.data.api

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
    suspend fun getLoginToken(@Body email: DomainUseUserIdRequest): DomainTokenResponse

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
}
