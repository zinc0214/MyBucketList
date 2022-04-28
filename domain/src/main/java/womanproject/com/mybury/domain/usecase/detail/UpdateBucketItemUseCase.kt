package womanproject.com.mybury.domain.usecase.detail

import okhttp3.MultipartBody
import womanproject.com.mybury.domain.usecase.fileToMultipartFile
import womanproject.com.mybury.domain.usecase.stringToMultipartFile
import womenproject.com.mybury.data.model.SimpleResponse
import womenproject.com.mybury.data.model.UpdateBucketItemInfo
import womenproject.com.mybury.data.repository.MyBuryRepository
import java.io.File
import javax.inject.Inject

class UpdateBucketItemUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend operator fun invoke(
        updateBucketItemInfo: UpdateBucketItemInfo
    ): SimpleResponse {

        val bucketItem = updateBucketItemInfo.content

        val title = bucketItem.title.stringToMultipartFile("title")
        val open = bucketItem.open.stringToMultipartFile("open")
        val dDate = bucketItem.dDate.stringToMultipartFile("dDate")
        val goalCount = bucketItem.goalCount.stringToMultipartFile("goalCount")
        val memo = bucketItem.memo.stringToMultipartFile("memo")
        val categoryId = bucketItem.categoryId.stringToMultipartFile("categoryId")
        val userId = updateBucketItemInfo.userId.stringToMultipartFile("userId")
        var image1: MultipartBody.Part? = null
        var image2: MultipartBody.Part? = null
        var image3: MultipartBody.Part? = null

        val removeImg1 =
            (updateBucketItemInfo.alreadyImgList[0] == null).stringToMultipartFile("removeImg1")
        val removeImg2 =
            (updateBucketItemInfo.alreadyImgList[1] == null).stringToMultipartFile("removeImg2")
        val removeImg3 =
            (updateBucketItemInfo.alreadyImgList[2] == null).stringToMultipartFile("removeImg3")

        val imgList = updateBucketItemInfo.imgList
        for (i in imgList.indices) {
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

        return myBuryRepository.updateBucketList(
            updateBucketItemInfo.token,
            updateBucketItemInfo.bucketId,
            title, open, dDate, goalCount,
            memo, categoryId, userId,
            image1, removeImg1, image2, removeImg2, image3, removeImg3
        )
    }
}