package womenproject.com.mybury.data.repository

import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Part
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

    suspend fun addBucketItem(
        @Header("X-Auth-Token") token: String,
        @Part title: MultipartBody.Part,
        @Part open: MultipartBody.Part,
        @Part dDate: MultipartBody.Part? = null,
        @Part goalCount: MultipartBody.Part,
        @Part memo: MultipartBody.Part,
        @Part categoryId: MultipartBody.Part,
        @Part userId: MultipartBody.Part,
        @Part image1: MultipartBody.Part? = null,
        @Part image2: MultipartBody.Part? = null,
        @Part image3: MultipartBody.Part? = null
    ): SimpleResponse
}