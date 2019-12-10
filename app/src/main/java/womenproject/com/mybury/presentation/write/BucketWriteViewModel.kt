package womenproject.com.mybury.presentation.write

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import womenproject.com.mybury.data.AddBucketItem
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.DetailBucketItem
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.util.fileListToMultipartFile
import womenproject.com.mybury.util.fileToMultipartFile
import womenproject.com.mybury.util.stringToMultipartFile
import java.io.File
import java.text.SimpleDateFormat

class BucketWriteViewModel : BaseViewModel() {

    var bucketItem: DetailBucketItem? = null

    interface OnBucketAddEvent {
        fun start()
        fun success()
        fun fail()
    }


    @SuppressLint("CheckResult")
    fun uploadBucketList(token: String, userId: String,
                         bucketItem: AddBucketItem,
                         imgList: MutableList<File>,
                         onBucketAddEvent: OnBucketAddEvent) {

        onBucketAddEvent.start()

        val title = bucketItem.title.stringToMultipartFile("title")
        val open = bucketItem.open.stringToMultipartFile("open")
        val dDate = bucketItem.dDate.stringToMultipartFile("dDate")
        val goalCount = bucketItem.goalCount.stringToMultipartFile("goalCount")
        val memo = bucketItem.memo.stringToMultipartFile("memo")
        val categoryId = bucketItem.categoryId.stringToMultipartFile("categoryId")
        val userId = userId.stringToMultipartFile("userId")

        val imageList = arrayListOf<MultipartBody.Part>()

        for(img in imgList) {
            imageList.add(img.fileToMultipartFile("multipartFiles"))
        }

        if(bucketItem.dDate.isBlank()) {
            apiInterface.postAddBucketListNotDDay(token, title, open, goalCount, memo, categoryId, userId, imageList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        Log.e("ayhan", "addBucketResponse : ${response.retcode}")
                        onBucketAddEvent.success()
                    }) {
                        Log.e("ayhan", "addBucketFail : $it")
                        onBucketAddEvent.fail()
                    }
        } else {
            apiInterface.postAddBucketList(token, title, open, dDate, goalCount, memo, categoryId, userId, imageList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        Log.e("ayhan", "addBucketResponse : ${response.retcode}")
                        onBucketAddEvent.success()
                    }) {
                        Log.e("ayhan", "addBucketFail : $it")
                        onBucketAddEvent.fail()
                    }
        }



/*        val bucketItem = bucketItem.stringToMultipartFile("bucketItem")

        apiInterface.postAddBucketList(token, bucketItem, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    uplaodBucketImage(imgList)
                }
                .subscribe({ response ->
                    Log.e("ayhan", "addBucketResponse : ${response.retcode}")
                    onBucketAddEvent.success()
                }) {
                    Log.e("ayhan", "addBucketFail : $it")
                    onBucketAddEvent.fail()
                }*/
    }


    @SuppressLint("CheckResult")
    fun uplaodBucketImage(imageList: MutableList<File>) {

        for (i in imageList) {
            apiInterface.postAddBucketImage(i.fileToMultipartFile("file"))
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


    fun isDateNotNull(): Boolean {
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