package womenproject.com.mybury.data

enum class SearchType {
    All, Category, DDay;

    fun getText(): String {
        return when (this) {
            All -> "전체"
            Category -> "카테고리"
            DDay -> "디데이"
        }
    }

    fun getLowerText(): String {
        return when (this) {
            All -> "all"
            Category -> "category"
            DDay -> "dday"
        }
    }

    fun toInt(): Int {
        return when (this) {
            All -> 0
            DDay -> 1
            Category -> 2
        }
    }
}

sealed class SearchResultType()