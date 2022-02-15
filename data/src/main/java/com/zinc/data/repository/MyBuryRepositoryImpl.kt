package com.zinc.data.repository

import com.zinc.data.api.MyBuryApi
import com.zinc.data.model.*
import javax.inject.Inject

class MyBuryRepositoryImpl @Inject constructor(
    private val myBuryApi: MyBuryApi
) : MyBuryRepository {
    override suspend fun signUpCheck(mail: SignUpCheckRequest): SignUpCheckResponse {
        return myBuryApi.signUpCheck(mail)
    }

    override suspend fun signUp(email: SignUpCheckRequest): SignUpResponse {
        return myBuryApi.signUp(email)
    }

    override suspend fun getLoginToken(usrdId: DomainUseUserIdRequest): DomainTokenResponse {
        return myBuryApi.getLoginToken(usrdId)
    }

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

    override suspend fun loadCategoryList(token: String, userId: String): BucketCategory {
        return myBuryApi.loadCategoryList(token, userId)
    }
}