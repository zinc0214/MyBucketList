package womenproject.com.mybury.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class DdayBucketListResponse(
    val dDayBucketlists: List<DdayBucketList>,
    val retcode: String
)

@Serializable
data class DdayBucketList(
    val day: Int,
    @SerializedName("bucketlists") val bucketLists: List<DomainBucketItem>,
    var isLast: Boolean = false
)

@Serializable
data class OriginMyPageInfo(
    val name: String,
    val imageUrl: String? = "",
    val startedCount: Int = 0,
    val completedCount: Int = 0,
    val dDayCount: Int = 0,
    val categoryList: List<OriginCategoryInfo>,
    val retcode: String
)

@Serializable
data class OriginCategoryInfo(
    val name: String,
    val id: String,
    val count: Int
)