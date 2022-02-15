package com.zinc.domain.usecase.category

import com.zinc.data.model.BucketCategory
import com.zinc.data.repository.MyBuryRepository
import javax.inject.Inject

class LoadCategoryListUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(token: String, userId: String): BucketCategory {
        return myBuryRepository.loadCategoryList(token, userId)
    }
}