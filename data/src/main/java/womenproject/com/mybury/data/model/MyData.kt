package womenproject.com.mybury.data.model

import com.google.gson.annotations.SerializedName

data class DdayBucketListResponse(
    val dDayBucketlists: List<DdayBucketList>,
    val retcode: String
)

data class DdayBucketList(
    val day: Int,
    @SerializedName("bucketlists") val bucketLists: List<DomainBucketItem>,
    var isLast: Boolean = false
)


