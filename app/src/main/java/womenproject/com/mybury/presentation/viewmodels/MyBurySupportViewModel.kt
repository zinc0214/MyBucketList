package womenproject.com.mybury.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import womenproject.com.mybury.data.PurchasedItem
import womenproject.com.mybury.data.PurchasedResult
import womenproject.com.mybury.data.SupportInfo
import womenproject.com.mybury.data.UseUserIdRequest
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class MyBurySupportViewModel @Inject constructor() : BaseViewModel() {

    private val _supportInfo = MutableLiveData<SupportInfo>()
    val supportInfo: LiveData<SupportInfo> = _supportInfo

    private val _supportPrice = MutableLiveData<String>()
    val supportPrice : LiveData<String> = _supportPrice

    fun getPurchasableItem() {
        if (accessToken == null || userId == null) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            val userIdRequest = UseUserIdRequest(userId)
            try {
                apiInterface.getSupportItem(accessToken, userIdRequest).apply {
                    withContext(Dispatchers.Main) {
                        when (this@apply.retcode) {
                            "200" -> _supportInfo.value = this@apply
                            "301" -> getRefreshToken(object : SimpleCallBack {
                                override fun success() {
                                    _supportInfo.value = this@apply
                                }

                                override fun fail() {

                                }
                            })
                            else -> {
                                // Do Nothing
                            }
                        }
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    // do nothing
                }
            }
        }
    }

    fun updateSupportPrice() {
        if (accessToken == null || userId == null) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            val userIdRequest = UseUserIdRequest(userId)
            try {
                apiInterface.getSupportItem(accessToken, userIdRequest).apply {
                    withContext(Dispatchers.Main) {
                        when (this@apply.retcode) {
                            "200" -> _supportPrice.value = this@apply.totalPrice
                            "301" -> getRefreshToken(object : SimpleCallBack {
                                override fun success() {
                                    _supportPrice.value = this@apply.totalPrice
                                }

                                override fun fail() {

                                }
                            })
                            else -> {
                                // Do Nothing
                            }
                        }
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    // do nothing
                }
            }
        }
    }


    fun purchasedItem(itemId: String, token: String, susYn: String, successCallBack: () -> Unit, fail: () -> Unit) {
        if (accessToken == null || userId == null) {
            fail.invoke()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val purchasedItem = PurchasedItem(itemId, userId, token, susYn)

            try {
                apiInterface.requestSupportItem(accessToken, purchasedItem).apply {
                    withContext(Dispatchers.Main) {
                        when (this@apply.retcode) {
                            "200" -> successCallBack.invoke()
                            "301" -> getRefreshToken(object : SimpleCallBack {
                                override fun success() {
                                    successCallBack.invoke()
                                }

                                override fun fail() {
                                    fail.invoke()
                                }
                            })
                            else -> fail.invoke()
                        }
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    fun editSuccessItem(token: String, susYn: String, success: () -> Unit, fail: () -> Unit) {
        if (accessToken == null || userId == null) {
            fail.invoke()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            val purchasedResult = PurchasedResult(userId, token, susYn)
            try {
                apiInterface.editSupportResult(accessToken, purchasedResult).apply {
                    withContext(Dispatchers.Main) {
                        when (this@apply.retcode) {
                            "200" -> success.invoke()
                            "301" -> getRefreshToken(object : SimpleCallBack {
                                override fun success() {
                                    success.invoke()
                                }
                                override fun fail() {
                                    fail.invoke()
                                }
                            })
                            else -> fail.invoke()
                        }
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    fail.invoke()
                }
            }
        }
    }
}
