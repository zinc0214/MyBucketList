package womenproject.com.mybury.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.CategoryInfo
import womenproject.com.mybury.data.SearchRequest
import womenproject.com.mybury.data.SearchResultType
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.data.network.apiInterface
import womenproject.com.mybury.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(): BaseViewModel() {

    private val _allBucketSearchResult = MutableLiveData<List<SearchResultType>>()
    val allBucketSearchResult: LiveData<List<SearchResultType>> = _allBucketSearchResult

    private val _searchSate = MutableLiveData<LoadState>()
    val searchSate: LiveData<LoadState> = _searchSate

    fun loadAllListSearch(searchType: String, searchText: String) {
        if (accessToken == null || userId == null) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val searchRequest = SearchRequest(
                userId = userId,
                filter = searchType,
                searchText = searchText
            )
            withContext(Dispatchers.Main) {
                try {
                    apiInterface.searchList(accessToken, searchRequest).apply {
                        val bucketList = this@apply.bucketlists
                        val categoryList = this@apply.categories

                        if (bucketList.isNullOrEmpty() && categoryList.isNullOrEmpty()) {
                            _searchSate.value = LoadState.FAIL
                            _allBucketSearchResult.value = emptyList()
                        } else {
                            _searchSate.value = LoadState.SUCCESS
                            _allBucketSearchResult.value =
                                parseToSearchTypeItem(bucketList, categoryList)
                        }
                    }
                } catch (e: Exception) {
                    _searchSate.value = LoadState.FAIL
                    _allBucketSearchResult.value = emptyList()
                }
            }
        }
    }

    private fun parseToSearchTypeItem(
        bucketList: List<BucketItem>?,
        categoryInfoList: List<CategoryInfo>?
    ): ArrayList<SearchResultType> {
        val resultList = arrayListOf<SearchResultType>()

        if (!bucketList.isNullOrEmpty()) {
            resultList.addAll(bucketList)
        }

        if (!categoryInfoList.isNullOrEmpty()) {
            resultList.addAll(categoryInfoList)
        }

        return resultList
    }

}