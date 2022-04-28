package womanproject.com.mybury.domain.usecase.detail

import womenproject.com.mybury.data.model.BucketRequest
import womenproject.com.mybury.data.model.SimpleResponse
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class CompleteBucketUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend operator fun invoke(
        token: String,
        bucketCompleteRequest: BucketRequest
    ): SimpleResponse {
        return myBuryRepository.completeBucket(token, bucketCompleteRequest)
    }
}