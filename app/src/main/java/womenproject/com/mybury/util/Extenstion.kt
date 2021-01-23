package womenproject.com.mybury.util

import android.content.Context
import android.widget.Toast

fun String.showToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}