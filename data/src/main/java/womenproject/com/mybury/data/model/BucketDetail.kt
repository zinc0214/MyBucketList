package womenproject.com.mybury.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BucketDetailItem(
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
) {


    fun isDone() = goalCount <= userCount

    fun isDdayShowable() = isDone().not() && dDay > 0

    fun ddayText() = if (dDay >= 0) dDay.toString() else dDay.toString().replace("-", "")

    fun isDdayMinus() = dDay >= 0

    fun isCategoryShowable() = category != "없음" && category.isNotBlank()

    fun isCountBucket() = goalCount > 1 && goalCount > userCount

    fun isNormalBucket() = !isDone().not() && userCount == 0

    fun isCommentShowable() : Boolean {
        val hasNoImg = imgUrl1.isNullOrBlank() && imgUrl2.isNullOrBlank() && imgUrl3.isNullOrBlank()
        return hasNoImg && isCountBucket().not() && isDone().not() && memo.isBlank()
    }

}