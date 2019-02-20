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
        var ddayBucketItemList: List<BucketItem>
)

data class BucketItem(
        val id : String = "id",
        var title : String = "Title",
        val open : Boolean = false,
        val pin : Boolean = false,
        val complete : Boolean = false,
        val category: BucketCategory,
        val user_count : Int = 0,
        val goal_count : Int = 0,
        val d_day : Int = 0
)

data class BucketCategory(
        val id : String = "",
        val name : String= ""
)