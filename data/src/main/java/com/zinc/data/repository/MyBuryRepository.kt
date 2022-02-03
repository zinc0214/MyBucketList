package com.zinc.data.repository

import com.zinc.data.model.BucketList

interface MyBuryRepository {
    suspend fun loadHomeBucketList(
        token: String,
        userId: String,
        filter: String,
        sort: String
    ) : BucketList
}