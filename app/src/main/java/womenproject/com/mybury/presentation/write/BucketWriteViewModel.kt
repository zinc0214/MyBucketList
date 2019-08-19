package womenproject.com.mybury.presentation.write

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.data.AddBucketItem
import womenproject.com.mybury.data.network.OkHttp3RetrofitManager
import womenproject.com.mybury.data.network.RetrofitInterface
import java.io.File

class BucketWriteViewModel : BaseViewModel() {

    val restClient: RetrofitInterface = OkHttp3RetrofitManager(BUCKETLIST_API).getRetrofitService(RetrofitInterface::class.java)

    companion object {
        private const val BUCKETLIST_API = "http://10.1.101.161/host/"
    }


    interface OnBucketAddEvent {
        fun start()
        fun success()
        fun fail()
    }




    fun addBucketList(bucketItem: AddBucketItem, imgList: MutableList<File>, onBucketAddEvent: OnBucketAddEvent) {

        onBucketAddEvent.start()

        val bucketListAddResult = restClient.postAddBucketList(bucketItem)
        bucketListAddResult.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    if(imgList.isEmpty()) {
                        Log.e("ayhan", "addBucketOK1")
                        onBucketAddEvent.success()
                    } else {
                        Log.e("ayhan", "addBucketImgGGO")
                        addBucketListImg(imgList, onBucketAddEvent)
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("ayhan2_addBucketFail", t.toString())
                onBucketAddEvent.fail()
            }
        })

    }


    private fun addBucketListImg(imgList :MutableList<File>, callback: OnBucketAddEvent) {

        Log.e("ayhan", "list : ${imgList.toString()}")

        for(img in imgList) {

            Log.e("ayhan", "img ::: $img")
        }

        val bucketListImgAddResult = restClient.postAddBucketImage(imgList)
        bucketListImgAddResult.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.e("ayhan", "addBucketOK2")
                    callback.success()
                } else {
                    Log.e("ayhan2_fail", "${response}")
                    callback.fail()
                }
            }


            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("ayhan2_addBucketList", t.toString())
                callback.fail()
            }



        })
    }



}