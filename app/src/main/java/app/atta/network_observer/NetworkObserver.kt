package app.atta.network_observer

import android.content.Context
import android.net.*
import android.os.Build
import androidx.annotation.RequiresPermission
import kotlin.properties.Delegates


class NetworkObserver(
    private val context: Context
) {

    private var onNetWorkStateChanged: ((available: Boolean) -> Unit)? = null

    private var netWorkStateChanged: Boolean? by Delegates.observable(null, { p, old, new ->
        if (old != new) {
            new?.let {
                onNetWorkStateChanged?.invoke(it)
            }
        }
    })

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            netWorkStateChanged = true
        }

        override fun onLost(network: Network) {
            netWorkStateChanged = false
        }
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected == true
        } else {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager
                .getNetworkCapabilities(network)
            (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED))
        }
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun registerNetworkCallback(callBack: ((available: Boolean) -> Unit)) {
        onNetWorkStateChanged = callBack
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder = NetworkRequest.Builder().apply {
            addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        }
        val networkRequest = builder.build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        netWorkStateChanged = isOnline()
    }

    fun unregisterNetworkCallback() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        onNetWorkStateChanged = null
        netWorkStateChanged = null
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}