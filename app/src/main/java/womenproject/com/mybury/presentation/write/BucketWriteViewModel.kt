package womenproject.com.mybury.presentation.write

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import womenproject.com.mybury.data.AddBucketItem
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.network.bucketListApi
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.util.fileToMultipartFile
import java.io.File

class BucketWriteViewModel : BaseViewModel() {

    var bucketItem : BucketItem  ? = null

    interface OnBucketAddEvent {
        fun start()
        fun success()
        fun fail()
    }

    @SuppressLint("CheckResult")
    fun uploadBucketList(bucketItem: AddBucketItem, imgList: MutableList<File>, onBucketAddEvent: OnBucketAddEvent) {

        onBucketAddEvent.start()

      //  uplaodBucketImage(imgList)

        bucketListApi.postAddBucketList(bucketItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    uplaodBucketImage(imgList)
                }
                .subscribe({ response ->
                    Log.e("ayhan", response.string())
                    onBucketAddEvent.success()
                }) {
                    Log.e("ayhan", it.toString())
                    onBucketAddEvent.fail()
                }
    }


    @SuppressLint("CheckResult")
    fun uplaodBucketImage(imageList: MutableList<File>) {

        for (i in imageList) {
            bucketListApi.postAddBucketImage(i.fileToMultipartFile())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        Log.e("ayhan_res", response.string())
                    }) {
                        Log.e("ayhan_throw", it.toString())
                    }

        }

    }

    fun getBucketDetailInfo() {

    }


    fun isDateNotNull() : Boolean {
        return bucketItem != null
    }
/*
    fun uploadBucketList(bucketItem: AddBucketItem, imgList: MutableList<File>, onBucketAddEvent: OnBucketAddEvent) {

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
*/


}