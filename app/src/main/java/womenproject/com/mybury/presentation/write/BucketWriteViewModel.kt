package womenproject.com.mybury.presentation.write

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import womenproject.com.mybury.BuildConfig
import womenproject.com.mybury.data.AddBucketItem
import womenproject.com.mybury.data.DetailBucketItem
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.util.fileToMultipartFile
import womenproject.com.mybury.util.stringToMultipartFile
import java.io.File

class BucketWriteViewModel : BaseViewModel() {

    var bucketItem: DetailBucketItem? = null
    val isOpenVisible = BuildConfig.DEBUG

    @SuppressLint("CheckResult")
    fun uploadBucketList(bucketItem: AddBucketItem,
                         imgList: MutableList<Any?>,
                         onBucketAddEvent: Simple3CallBack) {

        if(accessToken==null || userId == null) {
            onBucketAddEvent.fail()
            return
        }

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
            if (imgList[i] != null && imgList[i] is File) {
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
                    when (response.retcode) {
                        "200" -> {
                            onBucketAddEvent.success()
                        }
                        "301" -> getRefreshToken(object : SimpleCallBack {
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
                    Log.e("myBury", "addBucketFail : $it")
                    onBucketAddEvent.fail()
                }
    }


    @SuppressLint("CheckResult")
    fun updateBucketList(
            bucketId: String,
            bucketItem: AddBucketItem,
            alreadyImgList: MutableMap<Int, String?>,
            imgList: ArrayList<Any?>,
            onBucketAddEvent: Simple3CallBack) {

        if(accessToken==null || userId == null) {
            onBucketAddEvent.fail()
            return
        }

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

        var removeImg1 = (alreadyImgList[0]==null).stringToMultipartFile("removeImg1")
        var removeImg2 =(alreadyImgList[1]==null).stringToMultipartFile("removeImg2")
        var removeImg3 =(alreadyImgList[2]==null).stringToMultipartFile("removeImg3")

        for (i in 0 until imgList.size) {
            if (imgList[i] != null && imgList[i] is File) {
                val file = imgList[i] as File
                when (i) {
                    0 -> {
                        image1 = file.fileToMultipartFile("image1")
                    }
                    1 -> {
                        image2 = file.fileToMultipartFile("image2")
                    }
                    2 -> {
                        image3 = file.fileToMultipartFile("image3")
                    }
                }
            }
        }


        apiInterface.postUpdateBucketList(accessToken, bucketId,
                title, open, dDate, goalCount, memo, categoryId, p_userId,
                image1, removeImg1, image2, removeImg2, image3, removeImg3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    when (response.retcode) {
                        "200" -> {
                            onBucketAddEvent.success()
                        }
                        "301" -> getRefreshToken(object : SimpleCallBack {
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
                    Log.e("myBury", "addBucketFail : $it")
                    onBucketAddEvent.fail()
                }

    }


}