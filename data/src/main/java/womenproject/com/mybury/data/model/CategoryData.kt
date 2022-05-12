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

@Serializable
data class AddCategoryRequest(
    val userId: String,
    val name: String
)

@Serializable
data class EditCategoryNameRequest(
    val userId: String,
    val id: String,
    val name: String
)

@Serializable
data class ChangeCategoryStatusRequest(
    val userId: String,
    val categoryIdList: List<String>
)

@Serializable
data class RemoveCategoryRequest(
    val userId: String,
    val categoryIdList: List<String>
)