package com.zinc.data.repository

import com.zinc.data.model.*

interface MyBuryRepository {
    suspend fun signUpCheck(mail: SignUpCheckRequest): SignUpCheckResponse

    suspend fun signUp(email: SignUpCheckRequest): SignUpResponse

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