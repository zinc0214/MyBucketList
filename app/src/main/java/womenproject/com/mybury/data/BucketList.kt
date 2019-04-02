package womenproject.com.mybury.data

/**
 * Created by HanAYeon on 2018. 11. 27..
 */

data class BucketList(
        var bucketlists : List<BucketItem>,
        val popupYn : Boolean,
        val retcode : Int
)

data class DdayTotalBucketList(
        var ddayBucketEachListItem : List<DdayEachBucketGroup>
)

data class DdayEachBucketGroup(
        var dday: Int = 0,
        var ddayBucketItemList: List<BucketItem>,
        var isLast : Boolean = false
)

data class BucketItem(
        val id : String = "id",
        var title : String = "Title",
        val open : Boolean = false,
        val pin : Boolean = false,
        val complete : Boolean = false,
        val category: BucketCategory,
        val userCount : Int = 0,
        val goalCount : Int = 1,
        val dDay : Int = 0,
        var isLast : Boolean = false
)

data class AddBucketItem(
        var title : String = "Title",
        var memo : String = "Memo",
        var img : List<String>,
        val open : Boolean = false,
        val category: String,
        val dday : String,
        val goalCount : String
)

data class BucketCategory(
        val id : String = "",
        val name : String= ""
)

data class BucketUserCategory (
        val categoryList : List<String>
)

enum class CategoryType {
    notUse, categoryUse, categoryAdd
}