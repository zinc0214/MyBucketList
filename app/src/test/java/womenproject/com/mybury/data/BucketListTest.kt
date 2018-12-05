package womenproject.com.mybury.data

import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by HanAYeon on 2018. 12. 4..
 */
class BucketListTest {

    private lateinit var bucketList: BucketList
    @Before
    fun setUp() {
        bucketList = BucketList("JoeHyunSoo", "The Merciless")
    }

    @Test fun test_toString() {
        assertEquals("JoeHyunSoo", bucketList.name)
        assertEquals("The Merciless", bucketList.title)
    }
}