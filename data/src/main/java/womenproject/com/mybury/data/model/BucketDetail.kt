package womenproject.com.mybury.data.model

import kotlinx.serialization.Serializable

data class BucketDetailItem(
    var title: String,
    val memo: String = "",
    val open: Boolean = false,
    val category: String,
    val completedDt: String? = null,
    val userCount: Int = 0,
    val goalCount: Int = 0,
    val dDate: String?,
    val dDay: Int?,
    val imgUrl1: String? = null,
    val imgUrl2: String? = null,
    val imgUrl3: String? = null,
    val retcode: String
) : java.io.Serializable {

    fun isDone() = goalCount <= userCount

    fun isDdayShowable() = isDone().not() && isDdayMinus()

    fun ddayText() = if (isDdayMinus()) dDay.toString() else dDay.toString().replace("-", "")

    fun isDdayMinus() = dDay != null

    fun isCategoryShowable() = category != "없음" && category.isNotBlank()

    fun isCountBucket() = goalCount > 1 && goalCount > userCount

    fun isNormalBucket() = isDone().not() && userCount == 0 && goalCount == 1

    fun isCommentShowable(): Boolean {
        val hasNoImg = imgUrl1.isNullOrBlank() && imgUrl2.isNullOrBlank() && imgUrl3.isNullOrBlank()
        return hasNoImg && isCountBucket().not() && isDone().not() && memo.isBlank()
    }
}

data class BucketRequest(
    var bucketlistId: String
)

data class AddBucketItemInfo(
    val content : AddBucketItemContent,
    val imgList: List<Any?>,
    val userId : String,
    val token : String
)

@Serializable
data class AddBucketItemContent(
    var title: String,
    val open: Boolean = false,
    val dDate: String,
    val goalCount: Int = 0,
    var memo: String = "",
    val categoryId: String
)