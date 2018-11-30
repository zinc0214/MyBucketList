package womenproject.com.mybury.viewmodels

import androidx.lifecycle.ViewModel

/**
 * Created by HanAYeon on 2018. 11. 30..
 */

class BucketDetailViewModel internal constructor(private val bucketId: String) : ViewModel() {

    var bucketText = "$bucketId is Now Page~"

}