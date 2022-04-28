package womanproject.com.mybury.domain.usecase.detail

import okhttp3.MultipartBody
import womanproject.com.mybury.domain.usecase.fileToMultipartFile
import womanproject.com.mybury.domain.usecase.stringToMultipartFile
import womenproject.com.mybury.data.model.AddBucketItemInfo
import womenproject.com.mybury.data.model.SimpleResponse
import womenproject.com.mybury.data.repository.MyBuryRepository
import java.io.File
import javax.inject.Inject

class AddBucketItemUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend operator fun invoke(
        addBucketItemInfo: AddBucketItemInfo
    ): SimpleResponse {

        val bucketItem = addBucketItemInfo.content

        val title = bucketItem.title.stringToMultipartFile("title")
        val open = bucketItem.open.stringToMultipartFile("open")
        val dDate = bucketItem.dDate.stringToMultipartFile("dDate")
        val goalCount = bucketItem.goalCount.stringToMultipartFile("goalCount")
        val memo = bucketItem.memo.stringToMultipartFile("memo")
        val categoryId = bucketItem.categoryId.stringToMultipartFile("categoryId")
        val userId = addBucketItemInfo.userId.stringToMultipartFile("userId")
        var image1: MultipartBody.Part? = null
        var image2: MultipartBody.Part? = null
        var image3: MultipartBody.Part? = null
        for (i in 0 until addBucketItemInfo.imgList.size) {
            if (addBucketItemInfo.imgList[i] != null && addBucketItemInfo.imgList[i] is File) {
                val file = addBucketItemInfo.imgList[i] as File
                when (i) {
                    0 -> image1 = file.fileToMultipartFile("image1")
                    1 -> image2 = file.fileToMultipartFile("image2")
                    2 -> image3 = file.fileToMultipartFile("image3")
                }
            }
        }

        return myBuryRepository.addBucketItem(
            addBucketItemInfo.token,
            title, open, dDate, goalCount,
            memo, categoryId, userId,
            image1, image2, image3
        )
    }
}