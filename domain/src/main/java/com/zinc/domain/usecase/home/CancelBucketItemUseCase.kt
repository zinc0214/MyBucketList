package com.zinc.domain.usecase.home

import com.zinc.data.model.SimpleResponse
import com.zinc.data.model.StatusChangeBucketRequest
import com.zinc.data.repository.MyBuryRepository
import javax.inject.Inject

class CancelBucketItemUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(token: String, bucketRequest: StatusChangeBucketRequest): SimpleResponse {
        return myBuryRepository.cancelBucketItem(token, bucketRequest)
    }
}