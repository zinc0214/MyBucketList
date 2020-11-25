package womenproject.com.mybury.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import womenproject.com.mybury.data.PurchasedItem
import womenproject.com.mybury.data.SupportInfo
import womenproject.com.mybury.data.UseUserIdRequest
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel

class MyBurySupportViewModel : BaseViewModel() {

    private val _supportInfo = MutableLiveData<SupportInfo>()
    val supportInfo: LiveData<SupportInfo> = _supportInfo

    fun getPurchasableItem(fail: () -> Unit) {
        if (accessToken == null || userId == null) {
            fail.invoke()
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

    fun purchasedItem(itemId: String, successCallBack: () -> Unit, fail: () -> Unit) {
        if (accessToken == null || userId == null) {
            fail.invoke()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val purchasedItem = PurchasedItem(itemId, userId)

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
}
