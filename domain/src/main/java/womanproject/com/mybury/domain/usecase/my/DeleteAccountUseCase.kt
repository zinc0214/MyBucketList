package womanproject.com.mybury.domain.usecase.my

import womenproject.com.mybury.data.model.SimpleResponse
import womenproject.com.mybury.data.model.UseUserIdRequest
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend operator fun invoke(
        token: String,
        userIdRequest: UseUserIdRequest,
    ): SimpleResponse {
        return myBuryRepository.deleteAccount(token, userIdRequest)
    }
}