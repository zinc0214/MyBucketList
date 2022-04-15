package womanproject.com.mybury.domain.usecase.login

import womenproject.com.mybury.data.model.DomainTokenResponse
import womenproject.com.mybury.data.model.DomainUseUserIdRequest
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class LoadLoginTokenUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(userId: DomainUseUserIdRequest): DomainTokenResponse {
        return myBuryRepository.getLoginToken(userId)
    }
}

