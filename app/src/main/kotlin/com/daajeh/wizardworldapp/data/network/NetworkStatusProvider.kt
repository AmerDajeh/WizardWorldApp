package com.daajeh.wizardworldapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkStatusProvider(
    context: Context
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private fun networkConnectivity(): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            }

            else -> {
                connectivityManager.allNetworkInfo.any { it?.isConnected == true }
            }
        }
    }

    private suspend fun isConnectingToInternet(): Boolean {
        try {
            if (!networkConnectivity())
                return false

            val process = withContext(Dispatchers.IO) {
                Runtime.getRuntime().exec("ping -c 1 www.google.com")
            }
            val returnVal = withContext(Dispatchers.IO) {
                process.waitFor()
            }
            return returnVal == 0
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun isNetworkAvailable(): Boolean {
        return isConnectingToInternet()
    }
}
