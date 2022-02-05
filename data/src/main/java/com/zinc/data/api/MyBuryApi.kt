package com.zinc.data.api

import com.zinc.data.model.DomainBucketList
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MyBuryApi {

    @GET("/home")
    suspend fun loadHomeBucketList(
        @Header("X-Auth-Token") token: String,
        @Query("userId") userId: String,
        @Query("filter") filter: String,
        @Query("sort") sort: String
    ): DomainBucketList

}
