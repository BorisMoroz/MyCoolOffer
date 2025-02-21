package ru.practicum.android.diploma.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.delay
import ru.practicum.android.diploma.data.dto.Response

class RetrofitNetworkClient(
    private val hhApi: HHApi,
    private val connectivityManager: ConnectivityManager
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        val response = Response()
        try {
            if (!waitForConnection()) {
                response.apply { resultCode = Response.NETWORK_CONNECTION_ERROR }
            }
            //
            // Реализовать содержательные сетевые запросы
            //
            response.apply { resultCode = Response.OK }
        } catch (ex: NetworkRequestException) {
            Log.i("NetworkError", ex.message!!)
            response.apply { resultCode = Response.NETWORK_ERROR }
        }
        return response
    }

    private fun isConnected(): Boolean {
        var result = false
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> result = true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> result = true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> result = true
            }
        }
        return result
    }

    private suspend fun waitForConnection(): Boolean {
        repeat(Response.NETWORK_CONNECTION_RETRIES) {
            if (isConnected()) {
                return true
            }
            delay(Response.NETWORK_CONNECTION_DELAY)
        }
        return false
    }
}
