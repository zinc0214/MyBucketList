package womenproject.com.mybury.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import womenproject.com.mybury.data.BucketListRepository

/**
 * Created by HanAYeon on 2018. 11. 28..
 */
class MainFragmentViewModel : BaseViewModel() {

    val items: LiveData<List<String>> =
            MutableLiveData<List<String>>().apply { value = BucketListRepository().getItemsPage() }
}