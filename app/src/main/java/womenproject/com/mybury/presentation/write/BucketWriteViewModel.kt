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

    @SuppressLint("CheckResult")
    fun uploadBucketList(bucketItem: AddBucketItem,
                         imgList: MutableList<Any>,
                         onBucketAddEvent: Simple3CallBack) {

        onBucketAddEvent.start()

        val title = bucketItem.title.stringToMultipartFile("title")
        val open = bucketItem.open.stringToMultipartFile("open")
        val dDate = bucketItem.dDate.stringToMultipartFile("dDate")
        val goalCount = bucketItem.goalCount.stringToMultipartFile("goalCount")
        val memo = bucketItem.memo.stringToMultipartFile("memo")
        val categoryId = bucketItem.categoryId.stringToMultipartFile("categoryId")
        val p_userId = userId.stringToMultipartFile("userId")
        var image1: MultipartBody.Part? = null
        var image2: MultipartBody.Part? = null
        var image3: MultipartBody.Part? = null
        for (i in 0 until imgList.size) {
            if (imgList[i] is File) {
                val file = imgList[i] as File
                when (i) {
                    0 -> image1 = file.fileToMultipartFile("image1")
                    1 -> image2 = file.fileToMultipartFile("image2")
                    2 -> image3 = file.fileToMultipartFile("image3")
                }
            }
        }


        apiInterface.postAddBucketList(accessToken, title, open, dDate, goalCount, memo, categoryId, p_userId, image1, image2, image3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    when {
                        response.retcode == "200" -> {
                            Log.e("ayhan", "addBucketResponse : ${response.retcode}")
                            onBucketAddEvent.success()
                        }
                        response.retcode == "301" -> getRefreshToken(object : SimpleCallBack {
                            override fun success() {
                                onBucketAddEvent.restart()
                            }

                            override fun fail() {
                                onBucketAddEvent.fail()
                            }
                        })
                        else -> onBucketAddEvent.fail()
                    }

                }) {
                    Log.e("ayhan", "addBucketFail : $it")
                    onBucketAddEvent.fail()
                }
    }


    @SuppressLint("CheckResult")
    fun updateBucketList(
            bucketId: String,
            bucketItem: AddBucketItem,
            imgList: MutableList<Any>,
            onBucketAddEvent: Simple3CallBack) {

        onBucketAddEvent.start()

        val title = bucketItem.title.stringToMultipartFile("title")
        val open = bucketItem.open.stringToMultipartFile("open")
        val dDate = bucketItem.dDate.stringToMultipartFile("dDate")
        val goalCount = bucketItem.goalCount.stringToMultipartFile("goalCount")
        val memo = bucketItem.memo.stringToMultipartFile("memo")
        val categoryId = bucketItem.categoryId.stringToMultipartFile("categoryId")
        val p_userId = userId.stringToMultipartFile("userId")
        var image1: MultipartBody.Part? = null
        var image2: MultipartBody.Part? = null
        var image3: MultipartBody.Part? = null
        var noImg1 = true.stringToMultipartFile("noImg1")
        var noImg2 = true.stringToMultipartFile("noImg2")
        var noImg3 = true.stringToMultipartFile("noImg3")

        for (i in 0 until imgList.size) {
            if (imgList[i] is File) {
                val file = imgList[i] as File
                when (i) {
                    0 -> {
                        image1 = file.fileToMultipartFile("image1")
                        noImg1 = false.stringToMultipartFile("noImg1")
                    }
                    1 -> {
                        image2 = file.fileToMultipartFile("image2")
                        noImg2 = false.stringToMultipartFile("noImg1")
                    }
                    2 -> {
                        image3 = file.fileToMultipartFile("image3")
                        noImg3 = false.stringToMultipartFile("noImg1")
                    }
                }
            }
        }


        apiInterface.postUpdateBucketList(accessToken, bucketId,
                title, open, dDate, goalCount, memo, categoryId, p_userId,
                image1, noImg1, image2, noImg2, image3, noImg3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    when {
                        response.retcode == "200" -> {
                            Log.e("ayhan", "addBucketResponse : ${response.retcode}")
                            onBucketAddEvent.success()
                        }
                        response.retcode == "301" -> getRefreshToken(object : SimpleCallBack {
                            override fun success() {
                                onBucketAddEvent.restart()
                            }

                            override fun fail() {
                                onBucketAddEvent.fail()
                            }
                        })
                        else -> onBucketAddEvent.fail()
                    }

                }) {
                    Log.e("ayhan", "addBucketFail : $it")
                    onBucketAddEvent.fail()
                }

    }


}