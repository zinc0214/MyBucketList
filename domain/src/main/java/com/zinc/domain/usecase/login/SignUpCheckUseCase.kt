package com.zinc.domain.usecase.login

import com.zinc.data.model.SignUpCheckRequest
import com.zinc.data.model.SignUpCheckResponse
import com.zinc.data.repository.MyBuryRepository
import javax.inject.Inject

class SignUpCheckUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(signUpCheckRequest: SignUpCheckRequest): SignUpCheckResponse {
        return myBuryRepository.signUpCheck(signUpCheckRequest)
    }
}