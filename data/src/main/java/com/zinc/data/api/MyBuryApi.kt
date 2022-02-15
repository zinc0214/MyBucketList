package com.zinc.data.api

import com.zinc.data.model.*
import retrofit2.http.*

interface MyBuryApi {

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
}
