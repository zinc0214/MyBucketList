package womanproject.com.mybury.domain.usecase.my

import womenproject.com.mybury.data.model.OriginMyPageInfo
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class LoadMyPageInfoUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(
        token: String,
        userId: String,
    ): OriginMyPageInfo {
        return myBuryRepository.loadMyPageInfo(token, userId)
    }
}