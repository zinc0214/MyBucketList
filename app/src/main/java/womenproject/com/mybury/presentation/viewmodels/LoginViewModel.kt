package womenproject.com.mybury.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import womanproject.com.mybury.domain.usecase.login.LoadLoginTokenUseCase
import womanproject.com.mybury.domain.usecase.login.SignUpCheckUseCase
import womanproject.com.mybury.domain.usecase.login.SignUpUseCase
import womenproject.com.mybury.data.model.DomainUseUserIdRequest
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.data.model.SignUpCheckRequest
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signUpCheckUseCase: SignUpCheckUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val loadLoginTokenUseCase: LoadLoginTokenUseCase
) : BaseViewModel() {

    private val _checkForLoginResult = MutableLiveData<CheckForLoginResult>()
    val checkForLoginResult: LiveData<CheckForLoginResult> get() = _checkForLoginResult

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> get() = _signUpResult

    private val _loadLoginTokenResult = MutableLiveData<LoadState>()
    val loadLoginTokenResult: LiveData<LoadState> get() = _loadLoginTokenResult

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
                        _checkForLoginResult.value =
                            CheckForLoginResult.CreateAccount(account.email.toString())
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

    fun getLoginToken() {
        if(userId == null) {
            _loadLoginTokenResult.value = LoadState.FAIL
            return
        }

        viewModelScope.launch {
            runCatching {
                val getTokenRequest = DomainUseUserIdRequest(userId)
                loadLoginTokenUseCase.invoke(getTokenRequest).apply {
                    if (this.retcode == "200") {
                        _loadLoginTokenResult.value = LoadState.SUCCESS
                        setAccessToken(this.accessToken, this.refreshToken)

                    } else {
                        _loadLoginTokenResult.value = LoadState.RESTART
                    }
                }
            }.getOrElse {
                _loadLoginTokenResult.value = LoadState.FAIL
            }
        }
    }
}

sealed class CheckForLoginResult {
    object CheckFail : CheckForLoginResult()
    data class CreateAccount(val userEmail: String) : CheckForLoginResult()
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

sealed class LoadLoginTokenResult {
    object Fail : LoadLoginTokenResult()
    data class Success(
        val accessToken: String,
        val refreshToken: String
    ) : LoadLoginTokenResult()

}
