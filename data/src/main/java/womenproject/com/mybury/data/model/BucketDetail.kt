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
)