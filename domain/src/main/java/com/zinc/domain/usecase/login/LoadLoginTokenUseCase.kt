package com.zinc.domain.usecase.login

import com.zinc.data.model.DomainTokenResponse
import com.zinc.data.model.DomainUseUserIdRequest
import com.zinc.data.repository.MyBuryRepository
import javax.inject.Inject

class LoadLoginTokenUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(userId: DomainUseUserIdRequest): DomainTokenResponse {
        return myBuryRepository.getLoginToken(userId)
    }
}

