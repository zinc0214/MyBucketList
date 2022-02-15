package com.zinc.domain.usecase.login

import com.zinc.data.model.SignUpCheckRequest
import com.zinc.data.model.SignUpResponse
import com.zinc.data.repository.MyBuryRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(email: SignUpCheckRequest): SignUpResponse {
        return myBuryRepository.signUp(email)
    }
}