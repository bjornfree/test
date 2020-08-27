package com.bjornfree.tochka.loyalty.data.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.bjornfree.prices.utils.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.SocketTimeoutException

class NetworkConnectionInterceptor(context: Context) : Interceptor {
    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isInternetAvailable()) throw NoInternetException("Нет подключения к интернету!")
        return try {
            chain.proceed(chain.request())
        } catch (e: SocketTimeoutException){
            throw NoInternetException("Время ожидания вышло (Time out)")
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw   = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }
}