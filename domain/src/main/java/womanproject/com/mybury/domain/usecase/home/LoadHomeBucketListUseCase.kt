package womanproject.com.mybury.domain.usecase.home

import womenproject.com.mybury.data.model.DomainBucketList
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class LoadHomeBucketListUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(token: String, userId: String, filter: String, sort: String) : DomainBucketList {
        return myBuryRepository.loadHomeBucketList(token, userId, filter, sort)
    }
}