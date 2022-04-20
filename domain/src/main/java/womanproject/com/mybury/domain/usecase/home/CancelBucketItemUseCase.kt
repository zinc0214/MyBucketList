package womanproject.com.mybury.domain.usecase.home

import womenproject.com.mybury.data.model.SimpleResponse
import womenproject.com.mybury.data.model.StatusChangeBucketRequest
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class CancelBucketItemUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(token: String, bucketRequest: StatusChangeBucketRequest): SimpleResponse {
        return myBuryRepository.cancelBucketItem(token, bucketRequest)
    }
}