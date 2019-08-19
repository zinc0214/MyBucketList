package womenproject.com.mybury.presentation.write

import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.data.AdultCheck
import womenproject.com.mybury.data.network.RetrofitInterface


/**
 * Created by HanAYeon on 2018. 12. 3..
 */

class BucketWriteViewModelTest : BaseViewModel() {

    private val NAVER_ADULT_API = "https://openapi.naver.com/"
    var adultResult: AdultCheck? = null
    var isEnd = true
    var progressVisible = ObservableInt(View.GONE)
    var resultText = ObservableField<String>()
    lateinit var errorReason : String


    fun checkGo(adultCheckText: String) {
       checkAdultWithNaver(adultCheckText)
    }

    private fun checkAdultWithNaver(adultCheckText: String): AdultCheck? {

        progressVisible.set(View.VISIBLE)

       /* val restClient: RetrofitInterface = OkHttp3RetrofitManager(NAVER_ADULT_API).getRetrofitService(RetrofitInterface::class.java)

        val adultResultData = restClient.requestAdultResult(adultCheckText)
        adultResultData.enqueue(object : Callback<AdultCheck> {
            override fun onResponse(call: Call<AdultCheck>?, response: Response<AdultCheck>?) {
                if (response != null && response.isSuccessful) {
                    adultResult = response.body()!!
                    Log.e("ayhan:result", "${response.body()}")
                } else {
                    adultResult = null
                }
                checkFinish()
            }

            override fun onFailure(call: Call<AdultCheck>?, t: Throwable?) {
                Log.e("ayhan2", t.toString())
                errorReason = t.toString()
                adultResult = null
                checkFinish()
            }
        })
*/
        return adultResult

    }

    private fun checkFinish() {
        isEnd = true
        progressVisible.set(View.GONE)
        resultText.set(resultText())
    }

    private fun resultText() : String {
        return if(adultResult==null) {
            "Can't Checking now : $errorReason"
        } else {
            if(adultResult?.adult.equals("1")) {
                "This is Adult Result"
            } else {
                "This is Not Adult Result"
            }
        }
    }
}