package womenproject.com.mybury.data

/**
 * Created by HanAYeon on 2018. 11. 27..
 */

data class BucketList(
        val name : String = "Hi",
        val title : String = "Title"
) {

    override fun toString() = name
}