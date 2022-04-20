package womanproject.com.mybury.domain.usecase.login

import womenproject.com.mybury.data.model.SignUpCheckRequest
import womenproject.com.mybury.data.model.SignUpResponse
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(email: SignUpCheckRequest): SignUpResponse {
        return myBuryRepository.signUp(email)
    }
}