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
import womenproject.com.mybury.data.model.GetTokenResponse
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.data.model.SignUpCheckRequest
import womenproject.com.mybury.data.model.UseUserIdRequest
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

    private val _signUpResult = MutableLiveData<Pair<SignUpResult, String>>()
    val signUpResult: LiveData<Pair<SignUpResult, String>> get() = _signUpResult

    private val _loadLoginTokenResult = MutableLiveData<Pair<LoadState, GetTokenResponse?>>()
    val loadLoginTokenResult: LiveData<Pair<LoadState, GetTokenResponse?>> get() = _loadLoginTokenResult

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
                            _signUpResult.value = SignUpResult.Success to response.userId
                        }
                        "401" -> {
                            _signUpResult.value = SignUpResult.EmailExisted to ""
                        }
                        else -> {
                            _signUpResult.value = SignUpResult.Fail to ""
                        }
                    }
                }
            }.getOrElse {
                Log.e("myBury", "PostSignUpResponse Fail: ${it.message}")
                _signUpResult.value = SignUpResult.Fail to ""
            }
        }
    }

    fun getLoginToken(userId : String) {
        _loadLoginTokenResult.value = LoadState.START to null
        viewModelScope.launch {
            runCatching {
                val getTokenRequest = UseUserIdRequest(userId)
                loadLoginTokenUseCase.invoke(getTokenRequest).apply {
                    if (this.retcode == "200") {
                        _loadLoginTokenResult.value = LoadState.SUCCESS to this

                    } else {
                        _loadLoginTokenResult.value = LoadState.RESTART to null
                    }
                }
            }.getOrElse {
                _loadLoginTokenResult.value = LoadState.FAIL to null
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
    object Success : SignUpResult()
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
