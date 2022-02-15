package com.zinc.domain.usecase.home

import com.zinc.data.model.DomainBucketList
import com.zinc.data.repository.MyBuryRepository
import javax.inject.Inject

class LoadHomeBucketListUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(token: String, userId: String, filter: String, sort: String) : DomainBucketList {
        return myBuryRepository.loadHomeBucketList(token, userId, filter, sort)
    }
}