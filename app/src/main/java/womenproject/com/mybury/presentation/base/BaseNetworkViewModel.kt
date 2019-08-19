package womenproject.com.mybury.presentation.base

import womenproject.com.mybury.data.BucketList


/**
 * Created by HanAYeon on 2019. 3. 7..
 */

abstract class BaseNetworkViewModel() : BaseViewModel() {

    interface OnBucketListGetEvent {
        fun start()
        fun finish(bucketList: BucketList?)
    }

    abstract fun getData(api: String, callback: OnBucketListGetEvent) : BucketList?

}