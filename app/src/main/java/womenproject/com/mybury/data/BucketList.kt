package womenproject.com.mybury.data

/**
 * Created by HanAYeon on 2018. 11. 27..
 */

data class BucketList(
        var list : ArrayList<BucketItem>
)

data class BucketItem(
        var title : String = "Title",
        var count : Int = 0,
        var bucketType : Int = 0,
        var dday : Int = 0
)