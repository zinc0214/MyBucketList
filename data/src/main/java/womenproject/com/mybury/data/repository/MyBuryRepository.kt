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
        defaultImg: MultipartBody.Part,
        multipartFile: MultipartBody.Part? = null,
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
        token: String,
        title: MultipartBody.Part,
        open: MultipartBody.Part,
        dDate: MultipartBody.Part? = null,
        goalCount: MultipartBody.Part,
        memo: MultipartBody.Part,
        categoryId: MultipartBody.Part,
        userId: MultipartBody.Part,
        image1: MultipartBody.Part? = null,
        image2: MultipartBody.Part? = null,
        image3: MultipartBody.Part? = null
    ): SimpleResponse

    suspend fun updateBucketList(
        token: String,
        bucketId: String,
        title: MultipartBody.Part,
        open: MultipartBody.Part,
        dDate: MultipartBody.Part? = null,
        goalCount: MultipartBody.Part,
        memo: MultipartBody.Part,
        categoryId: MultipartBody.Part,
        userId: MultipartBody.Part,
        image1: MultipartBody.Part? = null,
        noImg1: MultipartBody.Part,
        image2: MultipartBody.Part? = null,
        noImg2: MultipartBody.Part,
        image3: MultipartBody.Part? = null,
        noImg3: MultipartBody.Part
    ): SimpleResponse

    suspend fun addNewCategoryItem(
        token: String,
        categoryId: AddCategoryRequest
    ): SimpleResponse

    suspend fun editCategoryItemName(
        token: String,
        categoryId: EditCategoryNameRequest
    ): SimpleResponse

    suspend fun changeCategoryList(
        token: String,
        categoryId: ChangeCategoryStatusRequest
    ): SimpleResponse

    suspend fun removeCategoryItem(
        token: String,
        removeCategoryRequest: RemoveCategoryRequest
    ): SimpleResponse

    suspend fun loadMyPageInfo(
        token: String,
        userId: String
    ): OriginMyPageInfo
}