package womenproject.com.mybury.data.repository

import okhttp3.MultipartBody
import womenproject.com.mybury.data.api.MyBuryApi
import womenproject.com.mybury.data.model.AddCategoryRequest
import womenproject.com.mybury.data.model.BucketCategory
import womenproject.com.mybury.data.model.BucketDetailItem
import womenproject.com.mybury.data.model.BucketRequest
import womenproject.com.mybury.data.model.ChangeCategoryStatusRequest
import womenproject.com.mybury.data.model.DdayBucketListResponse
import womenproject.com.mybury.data.model.DomainBucketList
import womenproject.com.mybury.data.model.EditCategoryNameRequest
import womenproject.com.mybury.data.model.GetTokenResponse
import womenproject.com.mybury.data.model.OriginMyPageInfo
import womenproject.com.mybury.data.model.RefreshTokenRequest
import womenproject.com.mybury.data.model.RefreshTokenResponse
import womenproject.com.mybury.data.model.RemoveCategoryRequest
import womenproject.com.mybury.data.model.SignUpCheckRequest
import womenproject.com.mybury.data.model.SignUpCheckResponse
import womenproject.com.mybury.data.model.SignUpResponse
import womenproject.com.mybury.data.model.SimpleResponse
import womenproject.com.mybury.data.model.StatusChangeBucketRequest
import womenproject.com.mybury.data.model.UseUserIdRequest
import womenproject.com.mybury.data.model.UserIdRequest
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

    override suspend fun getLoginToken(email: UseUserIdRequest): GetTokenResponse {
        return myBuryApi.getLoginToken(email)
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
        defaultImg: MultipartBody.Part,
        multipartFile: MultipartBody.Part?
    ): SimpleResponse {
        return myBuryApi.updateProfile(token, userId, name, defaultImg, multipartFile)
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

    override suspend fun addBucketItem(
        token: String,
        title: MultipartBody.Part,
        open: MultipartBody.Part,
        dDate: MultipartBody.Part?,
        goalCount: MultipartBody.Part,
        memo: MultipartBody.Part,
        categoryId: MultipartBody.Part,
        userId: MultipartBody.Part,
        image1: MultipartBody.Part?,
        image2: MultipartBody.Part?,
        image3: MultipartBody.Part?
    ): SimpleResponse {
        return myBuryApi.addBucketItem(
            token, title, open, dDate,
            goalCount, memo, categoryId, userId,
            image1, image2, image3
        )
    }

    override suspend fun updateBucketList(
        token: String,
        bucketId: String,
        title: MultipartBody.Part,
        open: MultipartBody.Part,
        dDate: MultipartBody.Part?,
        goalCount: MultipartBody.Part,
        memo: MultipartBody.Part,
        categoryId: MultipartBody.Part,
        userId: MultipartBody.Part,
        image1: MultipartBody.Part?,
        noImg1: MultipartBody.Part,
        image2: MultipartBody.Part?,
        noImg2: MultipartBody.Part,
        image3: MultipartBody.Part?,
        noImg3: MultipartBody.Part
    ): SimpleResponse {
        return myBuryApi.updateBucketList(
            token, bucketId,
            title, open, dDate, goalCount, memo, categoryId, userId,
            image1, noImg1, image2, noImg2, image3, noImg3
        )
    }

    override suspend fun addNewCategoryItem(
        token: String,
        categoryId: AddCategoryRequest
    ): SimpleResponse {
        return myBuryApi.addNewCategoryItem(token, categoryId)
    }

    override suspend fun editCategoryItemName(
        token: String,
        categoryId: EditCategoryNameRequest
    ): SimpleResponse {
        return myBuryApi.editCategoryItemName(token, categoryId)
    }

    override suspend fun changeCategoryList(
        token: String,
        categoryId: ChangeCategoryStatusRequest
    ): SimpleResponse {
        return myBuryApi.changeCategoryList(token, categoryId)
    }

    override suspend fun removeCategoryItem(
        token: String,
        removeCategoryRequest: RemoveCategoryRequest
    ): SimpleResponse {
        return myBuryApi.removeCategoryItem(token, removeCategoryRequest)
    }

    override suspend fun loadMyPageInfo(token: String, userId: String): OriginMyPageInfo {
        return myBuryApi.loadMyPageInfo(token, userId)
    }

    override suspend fun deleteAccount(token: String, userIdRequest: UseUserIdRequest): SimpleResponse {
        return myBuryApi.accountDelete(token, userIdRequest)
    }
}