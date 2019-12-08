package womenproject.com.mybury.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by HanAYeon on 2018. 11. 27..
 */

data class SignUpCheckRequest(
        var email : String
)

data class SignUpCheckResponse (
        val signUp : Boolean,
        val userId : String
)

data class SignUpResponse (
        val retcode : String,
        val user_id:String
)

data class GetTokenRequest(
        val userId : String
)

data class DefaulProfileImg(
        val bury : String = "bury",
        val my : String = "my"
)

data class GetTokenResponse(
        val accessToken : String,
        val refreshToken : String,
        val retcode : String
)

data class SimpleResponse(
        val retcode: String
)


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

@Parcelize
data class BucketItem(
        val id : String,
        var title : String,
        val memo  : String,
        val open : Boolean = false,
        val pin : Boolean = false,
        val complete : Boolean = false,
        val category: Category,
        val userCount : Int = 0,
        val goalCount : Int = 1,
        val dDay : Int = 0,
        var isLast : Boolean = false
) : Parcelable

data class AddBucketItem(
        var title : String = "Title",
        val open : Boolean = false,
        val dDate : Date,
        val goalCount : Int = 0,
        var memo : String = "Memo",
        val categoryId: String,
        val givenId : String
)

data class BucketCategory (
        val category : List<Category>,
        val retCode : String

)

@Parcelize
data class Category(
        val name: String,
        val id : String
) : Parcelable