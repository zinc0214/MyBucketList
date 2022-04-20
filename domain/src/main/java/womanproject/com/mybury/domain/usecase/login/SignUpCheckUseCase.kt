package womanproject.com.mybury.domain.usecase.login

import womenproject.com.mybury.data.model.SignUpCheckRequest
import womenproject.com.mybury.data.model.SignUpCheckResponse
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class SignUpCheckUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(signUpCheckRequest: SignUpCheckRequest): SignUpCheckResponse {
        return myBuryRepository.signUpCheck(signUpCheckRequest)
    }
}