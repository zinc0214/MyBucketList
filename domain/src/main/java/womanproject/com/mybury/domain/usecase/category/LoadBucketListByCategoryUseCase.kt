package womanproject.com.mybury.domain.usecase.category

import womenproject.com.mybury.data.model.DomainBucketList
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class LoadBucketListByCategoryUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend operator fun invoke(token: String, userId: String): DomainBucketList {
        return myBuryRepository.loadBucketListByCategory(token, userId)
    }
}