package com.zinc.data.repository

import com.zinc.data.api.MyBuryApi
import com.zinc.data.model.BucketList
import javax.inject.Inject

class MyBuryRepositoryImpl @Inject constructor(
    private val myBuryApi: MyBuryApi
) : MyBuryRepository {
    override suspend fun loadHomeBucketList(
        token: String,
        userId: String,
        filter: String,
        sort: String
    ): BucketList {
        return myBuryApi.loadHomeBucketList(token, userId, filter, sort)
    }
}