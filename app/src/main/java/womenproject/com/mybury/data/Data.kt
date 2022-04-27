package womenproject.com.mybury.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import womenproject.com.mybury.data.model.BucketType
import womenproject.com.mybury.data.model.DomainBucketItem
import womenproject.com.mybury.data.model.DomainCategory

/**
 * Created by HanAYeon on 2018. 11. 27..
 */

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

data class SimpleResponse(
    val retcode: String
)

@Parcelize
data class BucketList(
    var bucketlists: List<BucketItem>,
    val popupYn: Boolean,
    val retcode: String
) : Parcelable

@JvmName("toDataDomainBucketItem")
fun List<DomainBucketItem>.toBucketData(): List<BucketItem> {
    val list = arrayListOf<BucketItem>()
    this.forEach {
        list.add(
            BucketItem(
                id = it.id,
                title = it.title,
                userCount = it.userCount,
                goalCount = it.goalCount,
                dDay = it.dDay
            )
        )
    }
    return list
}

@Parcelize
data class BucketItem(
    val id: String,
    var title: String,
    var userCount: Int = 0,
    val goalCount: Int = 1,
    val dDay: Int?
) : Parcelable, SearchResultType() {
    fun getDdayText(): String {
        dDay?.let {
            return if (dDay < 0) "D${dDay.toString().replace("-", "+")}" else "D-${dDay}"
        }
        return ""
    }

    fun isOverDday(): Boolean {
        return if (dDay != null) dDay < 0 else false
    }

    fun bucketType(): BucketType {
        return when {
            userCount >= goalCount -> BucketType.SUCCEED_ITEM
            goalCount > 1 -> BucketType.COUNT_ITEM
            else -> BucketType.BASE_ITEM
        }
    }
}

@Parcelize
data class DetailBucketItem(
    var title: String,
    val memo: String = "",
    val open: Boolean = false,
    val category: String,
    val completedDt: String? = null,
    val userCount: Int = 0,
    val goalCount: Int = 0,
    val dDate: String?,
    val dDay: Int,
    val imgUrl1: String? = null,
    val imgUrl2: String? = null,
    val imgUrl3: String? = null,
    val retcode: String
) : Parcelable

@Parcelize
data class Category(
    val name: String,
    val id: String,
    val priority: Int = 0,
    val isDefault: String? = "N"
) : Parcelable

fun List<DomainCategory>.toCategoryData(): List<Category> {
    val list = arrayListOf<Category>()
    this.forEach {
        list.add(
            Category(
                name = it.name,
                id = it.id,
                priority = it.priority,
                isDefault = it.isDefault
            )
        )
    }
    return list
}

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
    val categoryList: List<CategoryInfo>,
    val retcode: String
) {
    fun showableCategoryList() : List<CategoryInfo> {
        return categoryList - categoryList.filter { it.count == 0 && it.name == "없음" }.toSet()
    }

    fun startedCountText() = startedCount.toString()

    fun completedCountText() = completedCount.toString()

    fun dDayCountText() = dDayCount.toString()
}

@Parcelize
data class CategoryInfo(
    val name: String,
    val id: String,
    val count: Int
) : Parcelable, SearchResultType() {
    fun getCountString(): String {
        return count.toString()
    }
}

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

@Parcelize
data class BucketListOrder(
    val userId: String,
    val orders: List<String>
) : Parcelable

@Parcelize
data class SearchRequest(
    val userId: String,
    val filter: String,
    val searchText: String
) : Parcelable

data class SearchResult(
    val bucketlists: List<BucketItem>?,
    val categories: List<CategoryInfo>?
)

enum class ShowFilter {
    all, completed, started
}

enum class DdayShowFilter {
    all, minus, plus
}

enum class SortFilter {
    updatedDt, createdDt, custom
}

enum class WebViewType {
    eula, privacy, openSource, notice
}