package womenproject.com.mybury.data

import java.util.*

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
        val category: CategoryList,
        val userCount : Int = 0,
        val goalCount : Int = 1,
        val dDay : Int = 0,
        var isLast : Boolean = false
)

data class AddBucketItem(
        var title : String = "Title",
        val open : Boolean = false,
        val dDate : Long,
        val goalCount : Int = 0,
        var memo : String = "Memo",
        val categoryName: String,
        val userId : String

)

data class BucketCategory (
        val categoryList : List<CategoryList>,
        val retCode : String

)

data class CategoryList(
        val name: String
)