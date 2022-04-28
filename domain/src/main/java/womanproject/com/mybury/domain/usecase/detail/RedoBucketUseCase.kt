package womanproject.com.mybury.domain.usecase.detail

import womenproject.com.mybury.data.model.SimpleResponse
import womenproject.com.mybury.data.model.StatusChangeBucketRequest
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class RedoBucketUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend operator fun invoke(
        token: String,
        statusChangeBucketRequest: StatusChangeBucketRequest
    ): SimpleResponse {
        return myBuryRepository.redoBucket(token, statusChangeBucketRequest)
    }
}