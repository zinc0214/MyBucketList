package com.zinc.data.api

import com.zinc.data.model.BucketCategory
import com.zinc.data.model.DomainBucketList
import com.zinc.data.model.SimpleResponse
import com.zinc.data.model.StatusChangeBucketRequest
import retrofit2.http.*

interface MyBuryApi {

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
