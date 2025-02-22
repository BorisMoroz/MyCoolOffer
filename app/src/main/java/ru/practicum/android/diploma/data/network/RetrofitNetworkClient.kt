package ru.practicum.android.diploma.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.util.NETWORK_CONNECTION_ERROR
import ru.practicum.android.diploma.util.NETWORK_ERROR
import ru.practicum.android.diploma.util.NETWORK_OK
import ru.practicum.android.diploma.util.UNKNOWN_REQUEST_ERROR

class RetrofitNetworkClient(
    private val hhApi: HHApi,
    private val connectivityManager: ConnectivityManager
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        var response = Response()
        try {
            if (!waitForConnection()) {
                return response.apply { resultCode = NETWORK_CONNECTION_ERROR }
            }
            when (dto) {
                is VacancySearchRequest -> {
                    withContext(Dispatchers.IO) {
                        response = hhApi.searchVacancies(dto.text)
                        response.apply { resultCode = NETWORK_OK }
                    }
                }
                else -> response.apply { resultCode = UNKNOWN_REQUEST_ERROR }
            }
        } catch (ex: NetworkRequestException) {
            Log.i("NetworkError", ex.message!!)
            response.apply { resultCode = NETWORK_ERROR }
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
