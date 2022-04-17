package womenproject.com.mybury.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

fun String.showToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}

fun Context.isConnectionOn(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return postAndroidMInternetCheck(connectivityManager)
}

private fun postAndroidMInternetCheck(connectivityManager: ConnectivityManager): Boolean {
    val network = connectivityManager.activeNetwork
    val connection =
        connectivityManager.getNetworkCapabilities(network)

    return connection != null && (
            connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
}