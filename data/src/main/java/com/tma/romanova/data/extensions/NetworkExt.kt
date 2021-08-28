package com.tma.romanova.data.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.tma.romanova.core.application

val connectivityManager by lazy{
    application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}

val isNetworkAbsent: Boolean
    get() {
        val n = connectivityManager.activeNetwork
        if (n != null) {
            val nc = connectivityManager.getNetworkCapabilities(n)
            return !((nc?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true) ||
                    (nc?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true))
        }
        return true
    }
