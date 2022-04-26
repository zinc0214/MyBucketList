package womenproject.com.mybury.data.repository

import okhttp3.MultipartBody
import womenproject.com.mybury.data.api.MyBuryApi
import womenproject.com.mybury.data.model.*
import javax.inject.Inject

class MyBuryRepositoryImpl @Inject constructor(
    private val myBuryApi: MyBuryApi
) : MyBuryRepository {

    override suspend fun getRefreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse {
        return myBuryApi.getRefreshToken(refreshTokenRequest)
    }

    override suspend fun signUpCheck(mail: SignUpCheckRequest): SignUpCheckResponse {
        return myBuryApi.signUpCheck(mail)
    }

    override suspend fun signUp(email: SignUpCheckRequest): SignUpResponse {
        return myBuryApi.signUp(email)
    }

    override suspend fun getLoginToken(usrdId: UseUserIdRequest): GetTokenResponse {
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

    override suspend fun loadBucketListByCategory(
        token: String,
        categoryId: String
    ): DomainBucketList {
        return myBuryApi.loadBucketListByCategory(token, categoryId)
    }

    override suspend fun loadDetailBucketInfo(
        token: String,
        bucketId: String,
        userId: String
    ): BucketDetailItem {
        return myBuryApi.loadDetailBucketInfo(token, bucketId, userId)
    }

    override suspend fun deleteBucket(
        token: String,
        userId: UserIdRequest,
        bucketId: String
    ): SimpleResponse {
        return myBuryApi.deleteBucket(token, userId, bucketId)
    }

    override suspend fun completeBucket(
        token: String,
        bucketCompleteRequest: BucketRequest
    ): SimpleResponse {
        return myBuryApi.completeBucket(token, bucketCompleteRequest)
    }

    override suspend fun updateProfile(
        token: String,
        userId: MultipartBody.Part,
        name: MultipartBody.Part,
        file: MultipartBody.Part?,
        defaultImg: MultipartBody.Part
    ): SimpleResponse {
        return myBuryApi.updateProfile(token, userId, name, file, defaultImg)
    }

    override suspend fun redoBucket(
        token: String,
        bucketRequest: StatusChangeBucketRequest
    ): SimpleResponse {
        return myBuryApi.redoBucket(token, bucketRequest)
    }

    override suspend fun loadDdayBucketList(
        token: String,
        userId: String,
        filter: String
    ): DdayBucketListResponse {
        return myBuryApi.loadDdayBucketList(token, userId, filter)
    }
}