package womanproject.com.mybury.domain.usecase.my

import womanproject.com.mybury.domain.usecase.fileToMultipartFile
import womanproject.com.mybury.domain.usecase.stringToMultipartFile
import womenproject.com.mybury.data.model.SimpleResponse
import womenproject.com.mybury.data.repository.MyBuryRepository
import java.io.File
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(
        token: String,
        _userId: String,
        _nickName: String,
        _defaultImg: Boolean,
        _file: File? = null,
    ): SimpleResponse {
        val userId = _userId.stringToMultipartFile("userId")
        val nickName = _nickName.stringToMultipartFile("name")
        val defaultImg = _defaultImg.stringToMultipartFile("defaultImg")
        val profileImg = _file?.fileToMultipartFile("multipartFile")

        return myBuryRepository.updateProfile(token, userId, nickName, defaultImg, profileImg)
    }
}