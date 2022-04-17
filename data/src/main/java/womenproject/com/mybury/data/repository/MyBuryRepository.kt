package womenproject.com.mybury.data.repository

import womenproject.com.mybury.data.model.*

interface MyBuryRepository {
    suspend fun getRefreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse

    suspend fun signUpCheck(mail: SignUpCheckRequest): SignUpCheckResponse

    suspend fun signUp(email: SignUpCheckRequest): SignUpResponse

    suspend fun getLoginToken(email: DomainUseUserIdRequest): DomainTokenResponse

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

    suspend fun loadBucketListByCategory(
        token: String,
        categoryId: String
    ): DomainBucketList

    suspend fun loadDetailBucketInfo(
        token: String,
        bucketId: String,
        userId: String
    ): BucketDetailItem
}