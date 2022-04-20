package womanproject.com.mybury.domain.usecase.detail

import womenproject.com.mybury.data.model.BucketDetailItem
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class LoadBucketDetailItemUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend operator fun invoke(token: String, bucketId: String, userId: String): BucketDetailItem {
        return myBuryRepository.loadDetailBucketInfo(token, bucketId, userId)
    }
}