package com.zinc.data.repository

import com.zinc.data.api.MyBuryApi
import com.zinc.data.model.DomainBucketList
import com.zinc.data.model.SimpleResponse
import com.zinc.data.model.StatusChangeBucketRequest
import javax.inject.Inject

class MyBuryRepositoryImpl @Inject constructor(
    private val myBuryApi: MyBuryApi
) : MyBuryRepository {
    override suspend fun loadHomeBucketList(
        token: String,
        userId: String,
        filter: String,
        sort: String
    ): DomainBucketList {
        return myBuryApi.loadHomeBucketList(token, userId, filter, sort)
    }

    override suspend fun cancelBucketItem(
        token: String,
        bucketRequest: StatusChangeBucketRequest
    ): SimpleResponse {
        return myBuryApi.cancelBucketItem(token, bucketRequest)
    }
}