package womenproject.com.mybury.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.zinc.data.model.SignUpCheckRequest
import com.zinc.domain.usecase.login.SignUpCheckUseCase
import com.zinc.domain.usecase.login.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signUpCheckUseCase: SignUpCheckUseCase,
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel() {

    private val _checkForLoginResult = MutableLiveData<CheckForLoginResult>()
    val checkForLoginResult: LiveData<CheckForLoginResult> get() = _checkForLoginResult

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> get() = _signUpResult

    fun checkForIsFirstLogin(account: GoogleSignInAccount) {
        viewModelScope.launch {
            runCatching {
                val emailDataClass = SignUpCheckRequest(account.email.toString())
                signUpCheckUseCase.invoke(emailDataClass).apply {
                    val response = this
                    if (response.signUp) {
                        _checkForLoginResult.value =
                            CheckForLoginResult.HasAccount(
                                response.userId,
                                account.email.toString(),
                                true
                            )
                    } else {
                        _checkForLoginResult.value = CheckForLoginResult.CreateAccount
                    }
                }
            }.getOrElse {
                _checkForLoginResult.value = CheckForLoginResult.CheckFail
                Log.e("myBury", "getCheckForIsFirstLogin Fail ${it.message}")
            }
        }
    }

    fun loadUserId(email: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                val signUpCheckRequest = SignUpCheckRequest(email)
                signUpUseCase.invoke(signUpCheckRequest).apply {
                    val response = this
                    when (response.retcode) {
                        "200" -> {
                            _signUpResult.value = SignUpResult.Success(response.userId)
                        }
                        "401" -> {
                            _signUpResult.value = SignUpResult.EmailExisted
                        }
                        else -> {
                            _signUpResult.value = SignUpResult.Fail
                        }
                    }
                }
            }.getOrElse {
                Log.e("myBury", "PostSignUpResponse Fail: ${it.message}")
                _signUpResult.value = SignUpResult.Fail
            }
        }
    }
}

sealed class CheckForLoginResult {
    object CreateAccount : CheckForLoginResult()
    object CheckFail : CheckForLoginResult()
    data class HasAccount(
        val userId: String,
        val userEmail: String,
        val isLoginComplete: Boolean
    ) : CheckForLoginResult()
}

sealed class SignUpResult {
    data class Success(
        val userId: String
    ) : SignUpResult()

    object EmailExisted : SignUpResult()
    object Fail : SignUpResult()
}