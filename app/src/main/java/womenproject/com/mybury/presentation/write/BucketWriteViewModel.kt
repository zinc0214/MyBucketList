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
    var updateBucketItem: BucketItem? = null

    interface OnBucketAddEvent {
        fun start()
        fun success()
        fun fail()
    }


    @SuppressLint("CheckResult")
    fun uploadBucketList(token: String, userId: String,
                         bucketItem: AddBucketItem,
                         imgList: MutableList<Any>,
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

        for (img in imgList) {
            if (img is File) {
                imageList.add(img.fileToMultipartFile("multipartFiles"))
            }

        }

        if (bucketItem.dDate.isBlank()) {
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
    }


    @SuppressLint("CheckResult")
    fun updateBucketList(token: String, userId: String,
                         bucketId: String,
                         bucketItem: AddBucketItem,
                         imgList: MutableList<Any>,
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

        for (img in imgList) {
            if (img is File) {
                imageList.add(img.fileToMultipartFile("multipartFiles"))
            }
        }

        if (bucketItem.dDate.isBlank()) {
            apiInterface.postUpdateBucketListNotDDay(token, bucketId, title, open, goalCount, memo, categoryId, userId, imageList)
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
            apiInterface.postUpdateBucketList(token, bucketId, title, open, dDate, goalCount, memo, categoryId, userId, imageList)
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
    }


}