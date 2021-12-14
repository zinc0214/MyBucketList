package womenproject.com.mybury.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.presentation.base.BaseViewModel

class SearchViewModel : BaseViewModel() {

    private val _allBucketSearchResult = MutableLiveData<List<BucketItem>>()
    val allBucketSearchResult: LiveData<List<BucketItem>> = _allBucketSearchResult

    fun loadAllListSearch() {
        if (accessToken == null || userId == null) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                try {
                    _allBucketSearchResult.value = items()
                    Log.e("ayhan", "here : ${_allBucketSearchResult.value}")
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        // do nothing
                    }
                }
            }
        }
    }

    private fun items(): List<BucketItem> {
        return listOf(
            BucketItem(
                id = "1",
                category = Category(
                    name = "H",
                    id = "",
                    priority = 0
                ),
                memo = "",
                dDay = 10,
                title = "12346"
            ),
            BucketItem(
                id = "1",
                category = Category(
                    name = "H",
                    id = "",
                    priority = 0
                ),
                userCount = 10,
                goalCount = 20,
                memo = "",
                dDay = 10,
                title = "12346"
            )
        )
    }

}