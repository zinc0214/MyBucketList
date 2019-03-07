package womenproject.com.mybury.base

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import womenproject.com.mybury.MyBuryApplication

/**
 * Created by HanAYeon on 2019. 3. 7..
 */

open class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun isNetworkDisconnect(): Boolean {
        val cm = MyBuryApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        return !isConnected
    }

}