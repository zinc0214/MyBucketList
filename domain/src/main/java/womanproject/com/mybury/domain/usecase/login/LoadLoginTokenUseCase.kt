package womanproject.com.mybury.domain.usecase.login

import womenproject.com.mybury.data.model.GetTokenResponse
import womenproject.com.mybury.data.model.UseUserIdRequest
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class LoadLoginTokenUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(userId: UseUserIdRequest): GetTokenResponse {
        return myBuryRepository.getLoginToken(userId)
    }
}

