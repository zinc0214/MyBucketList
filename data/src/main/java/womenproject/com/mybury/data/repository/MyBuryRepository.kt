package womenproject.com.mybury.data.repository

import okhttp3.MultipartBody
import womenproject.com.mybury.data.model.*

interface MyBuryRepository {
    suspend fun getRefreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse

    suspend fun signUpCheck(mail: SignUpCheckRequest): SignUpCheckResponse

    suspend fun signUp(email: SignUpCheckRequest): SignUpResponse

    suspend fun getLoginToken(email: UseUserIdRequest): GetTokenResponse

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

    suspend fun deleteBucket(
        token: String,
        userId: UserIdRequest,
        bucketId: String
    ): SimpleResponse

    suspend fun completeBucket(
        token: String,
        bucketCompleteRequest: BucketRequest
    ): SimpleResponse

    suspend fun updateProfile(
        token: String,
        userId: MultipartBody.Part,
        name: MultipartBody.Part,
        file: MultipartBody.Part? = null,
        defaultImg: MultipartBody.Part
    ): SimpleResponse

    suspend fun redoBucket(
        token: String,
        bucketRequest: StatusChangeBucketRequest
    ): SimpleResponse

    suspend fun loadDdayBucketList(
        token: String,
        userId: String,
        filter: String
    ): DdayBucketListResponse
}