package womenproject.com.mybury.data

/**
 * Created by HanAYeon on 2018. 11. 27..
 */

data class BucketList(
        var list : ArrayList<BucketItem>
)

data class DdayTotalBucketList(
        var ddayBucketEachListItem : List<DdayEachBucketGroup>
)

data class DdayEachBucketGroup(
        var dday: Int = 0,
        var ddayBucketItemList: List<BucketItem>
)

data class BucketItem(
        var title : String = "Title", // 제목
        var count : Int = 0, // 완료 = 0, 1회 = 1, n회 = n
        var dday : Int = 0, // 남은 dday 수
        var ddayVisible : Boolean = false // 메인 화면에 dday 색상을 표시할 것인지의 여부
)