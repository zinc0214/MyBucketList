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

        val imageList = arrayListOf<MultipartBody.Part>()

        for (img in imgList) {
            if (img is File) {
                imageList.add(img.fileToMultipartFile("multipartFiles"))
            }

        }

        if (bucketItem.dDate.isBlank()) {
            apiInterface.postAddBucketListNotDDay(accessToken, title, open, goalCount, memo, categoryId, p_userId, imageList)
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
        } else {
            apiInterface.postAddBucketList(accessToken, title, open, dDate, goalCount, memo, categoryId, p_userId, imageList)
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

        val imageList = arrayListOf<MultipartBody.Part>()

        for (img in imgList) {
            if (img is File) {
                imageList.add(img.fileToMultipartFile("multipartFiles"))
            }
        }

        if (bucketItem.dDate.isBlank()) {
            apiInterface.postUpdateBucketListNotDDay(accessToken, bucketId, title, open, goalCount, memo, categoryId, p_userId, imageList)
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
        } else {
            apiInterface.postUpdateBucketList(accessToken, bucketId, title, open, dDate, goalCount, memo, categoryId, p_userId, imageList)
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


}