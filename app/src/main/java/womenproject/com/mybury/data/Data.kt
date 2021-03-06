package womenproject.com.mybury.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by HanAYeon on 2018. 11. 27..
 */

data class SignUpCheckRequest(
        var email: String
)

data class SignUpCheckResponse(
        val signUp: Boolean,
        val userId: String
)

data class SignUpResponse(
        val retcode: String,
        val userId: String
)

data class UseUserIdRequest(
        val userId: String?
)

data class DefaulProfileImg(
        val bury: String = "bury",
        val my: String = "my"
)

data class GetTokenResponse(
        val accessToken: String,
        val refreshToken: String,
        val retcode: String
)

data class NewTokenRequest(
        val userId: String?,
        val refreshToken: String?
)

data class BucketRequest(
        var bucketlistId: String
)

data class StatusChangeBucketRequest(
        val userId: String?,
        var bucketlistId: String
)

data class SimpleResponse(
        val retcode: String
)


data class BucketList(
        var bucketlists: List<BucketItem>,
        val popupYn: Boolean,
        val retcode: String
)

data class DdayBucketListRespone(
        val dDayBucketlists: List<DdayBucketList>,
        val retcode: String
)

data class DdayBucketList(
        val day: Int,
        val bucketlists: List<BucketItem>,
        var isLast: Boolean = false
)


@Parcelize
data class BucketItem(
        val id: String,
        var title: String,
        val memo: String,
        val open: Boolean = false,
        val pin: Boolean = false,
        val category: Category,
        var userCount: Int = 0,
        val goalCount: Int = 1,
        val dDay: Int?
) : Parcelable

@Parcelize
data class DetailBucketItem(
        var title: String,
        val memo: String = "",
        val open: Boolean = false,
        val category: String,
        val completedDt : String ?= null,
        val userCount: Int = 0,
        val goalCount: Int = 0,
        val dDate: String?,
        val dDay: Int,
        val imgUrl1: String? = null,
        val imgUrl2: String? = null,
        val imgUrl3: String? = null,
        val retcode: String
) : Parcelable

data class AddBucketItem(
        var title: String = "Title",
        val open: Boolean = false,
        val dDate: String,
        val goalCount: Int = 0,
        var memo: String = "",
        val categoryId: String
)

data class BucketCategory(
        val categoryList: List<Category>,
        val retcode: String
)

@Parcelize
data class Category(
        val name: String,
        val id: String,
        val priority: Int = 0
) : Parcelable

data class AddCategoryRequest(
        val userId: String,
        val name: String
)

data class EditCategoryNameRequest(
        val userId: String,
        val id: String,
        val name: String
)

data class ChangeCategoryStatusRequest(
        val userId: String,
        val categoryIdList: List<String>
)

data class RemoveCategoryRequest(
        val userId: String,
        val categoryIdList: List<String>
)

data class MyPageInfo(
        val name: String,
        val imageUrl: String? = "",
        val startedCount: Int = 0,
        val completedCount: Int = 0,
        val dDayCount: Int = 0,
        val categoryList: List<MyPageCategory>,
        val retcode: String
)

@Parcelize
data class MyPageCategory(
        val name: String,
        val id: String,
        val count: Int
) : Parcelable

@Parcelize
data class SupportInfo(
        val supportItems: List<PurchasableItem>,
        var totalPrice: String = "0",
        val recentSupport: List<RecentSupport>,
        val retcode: String
) : Parcelable

@Parcelize
data class PurchasableItem(
        val id: String,
        val itemName: String,
        val itemPrice: String,
        val itemImg: String,
        val googleKey: String,
        var dpYn: String,  //결제 성공여부
        var isPurchasable: Boolean = true
) : Parcelable

@Parcelize
data class RecentSupport(
        val seq: Int,
        val itemId: String,
        var userId: String,
        val token: String,
        val susYn: String
) : Parcelable

@Parcelize
data class PurchasedItem(
        val itemId: String,
        val userId: String,
        val token: String,
        val susYn: String
) : Parcelable

data class PurchasedResult(
        val userId: String,
        val token: String,
        val susYn: String
)

enum class ShowFilter {
    all, completed, started
}

enum class DdayShowFilter {
    all, minus, plus
}


enum class ListUpFilter {
    updatedDt, createdDt
}

enum class DataTextType {
    eula, privacy, openSource
}

val SUCCEED_ITEM = 0
val COUNT_ITEM = 1
val BASE_ITEM = 2

val DDAY = true
val NORMAL = false
