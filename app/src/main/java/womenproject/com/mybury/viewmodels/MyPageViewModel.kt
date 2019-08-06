package womenproject.com.mybury.viewmodels

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import womenproject.com.mybury.base.BaseViewModel
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.network.OkHttp3RetrofitManager
import womenproject.com.mybury.network.RetrofitInterface

/**
 * Created by HanAYeon on 2019. 4. 23..
 */

class MyPageViewModel : BaseViewModel() {

    var nickname = "닉네임이 이렇게 길다리다링루룽"
    var doingBucketCount = "27"
    var doneBucketCount = "31"
    var ddayCount = "20"

}