package womanproject.com.mybury.domain.usecase.my

import womenproject.com.mybury.data.model.DdayBucketListResponse
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class LoadDdayBucketListUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend operator fun invoke(
        token: String,
        userId: String,
        filter: String
    ): DdayBucketListResponse {
        return myBuryRepository.loadDdayBucketList(token, userId, filter)
    }
}