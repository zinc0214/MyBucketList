package womenproject.com.mybury.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BucketCategory(
    val categoryList: List<DomainCategory>,
    val retcode: String
)

@Serializable
data class DomainCategory(
    val name: String,
    val id: String,
    val priority: Int = 0,
    val isDefault: String? = "N"
)