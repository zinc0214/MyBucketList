package womenproject.com.mybury.adapter

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

/**
 * Created by HanAYeon on 2018. 12. 3..
 */

@BindingAdapter("isGone")
fun bindIsGone(view: ProgressBar, isGone: Boolean?) {
    if (isGone!!) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}