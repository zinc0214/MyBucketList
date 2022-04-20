package womanproject.com.mybury.domain.usecase.detail

import womenproject.com.mybury.data.model.SimpleResponse
import womenproject.com.mybury.data.model.UserIdRequest
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class DeleteBucketUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend operator fun invoke(token: String, userId: UserIdRequest, bucketId: String): SimpleResponse {
        return myBuryRepository.deleteBucket(token, userId, bucketId)
    }
}