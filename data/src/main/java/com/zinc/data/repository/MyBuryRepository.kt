package com.zinc.data.repository

import com.zinc.data.model.BucketCategory
import com.zinc.data.model.DomainBucketList
import com.zinc.data.model.SimpleResponse
import com.zinc.data.model.StatusChangeBucketRequest

interface MyBuryRepository {
    suspend fun loadHomeBucketList(
        token: String,
        userId: String,
        filter: String,
        sort: String
    ): DomainBucketList

    suspend fun cancelBucketItem(
        token: String,
        bucketRequest: StatusChangeBucketRequest
    ): SimpleResponse

    suspend fun loadCategoryList(
        token: String,
        userId: String
    ): BucketCategory
}