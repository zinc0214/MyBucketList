package womenproject.com.mybury.presentation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import womenproject.com.mybury.data.PurchasableItem
import womenproject.com.mybury.presentation.base.BaseViewModel

class MyBurySupportViewModel : BaseViewModel() {

    private val _purchaseItem = MutableLiveData<List<PurchasableItem>>()
    val purchaseItem: LiveData<List<PurchasableItem>> = _purchaseItem

    @SuppressLint("CheckResult")
    fun getPurchasableItem(fail: () -> Unit) {
        if (accessToken == null || userId == null) {
            fail()
            return
        }
        Log.e("ayhan", "isHere?")
        val iceCream = PurchasableItem("ice_cream", "차가운 아이스크림", "1000")
        val chicken = PurchasableItem("chicken", "존맛탱 치킨", "2000")
        val hamburger = PurchasableItem("hamburger", "아무튼 햄버거", "3000")

        _purchaseItem.value = listOf(iceCream, chicken, hamburger, chicken, hamburger, hamburger, iceCream)

        Log.e("ayhan", " _purchaseItem.value : ${_purchaseItem.value!!.size}")

        /* apiInterface.requestBeforeWrite(accessToken, userId)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe({ response ->
                     when (response.retcode) {
                         "200" -> {

                         }
                         "301" -> retry()
                         else -> fail()
                     }
                 }) {
                     Log.e("myBury", "getCategoryListFail : $it")
                     fail()
                 }*/
    }
}
