package com.zinc.data.repository

import com.zinc.data.model.DomainBucketList

interface MyBuryRepository {
    suspend fun loadHomeBucketList(
        token: String,
        userId: String,
        filter: String,
        sort: String
    ) : DomainBucketList
}