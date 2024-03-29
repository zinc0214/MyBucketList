package womenproject.com.mybury.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

inline fun <T : Any> LiveData<out T?>.observeNonNull(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) {
    this.observe(owner) {
        if (it != null) {
            observer(it)
        }
    }
}
