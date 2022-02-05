package com.zinc.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DomainBucketList(
    var bucketlists: List<DomainBucketItem>,
    val popupYn: Boolean,
    val retcode: String
)

@Serializable
data class DomainBucketItem(
    val id: String,
    var title: String,
    var userCount: Int = 0,
    val goalCount: Int = 1,
    val dDay: Int?
)