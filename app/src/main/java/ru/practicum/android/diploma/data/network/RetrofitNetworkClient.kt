package ru.practicum.android.diploma.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.requests.AreasRequest
import ru.practicum.android.diploma.data.dto.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.requests.VacancyDetailsRequest
import ru.practicum.android.diploma.data.dto.responses.Response
import ru.practicum.android.diploma.util.NETWORK_CONNECTION_ERROR
import ru.practicum.android.diploma.util.NETWORK_ERROR
import ru.practicum.android.diploma.util.NETWORK_OK
import ru.practicum.android.diploma.util.UNKNOWN_REQUEST_ERROR
import java.io.IOException

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
            response = doActionOnRequest(dto)
        } catch (ex: IOException) {
            Log.i("NetworkIOError", ex.message!!)
            response.apply { resultCode = NETWORK_ERROR }
        } catch (ex: HttpException) {
            Log.i("NetworkHTTPError", ex.message!!)
            response.apply { resultCode = ex.code() }
        }
        return response
    }

    private suspend fun doActionOnRequest(dto: Any): Response {
        var response = Response()

        when (dto) {
            is VacanciesSearchRequest -> {
                withContext(Dispatchers.IO) {
                    response = hhApi.searchVacancies(dto.text, dto.page, dto.perPage)
                    response.apply { resultCode = NETWORK_OK }
                }
            }

            is VacancyDetailsRequest -> {
                withContext(Dispatchers.IO) {
                    response = hhApi.getVacancyDetails(dto.vacancyId)
                    response.apply { resultCode = NETWORK_OK }
                }
            }

            is AreasRequest -> {
                withContext(Dispatchers.IO) {
                    response = hhApi.getAreas(dto.id)
                    response.apply { resultCode = NETWORK_OK }
                }
            }

            else -> response = response.apply { resultCode = UNKNOWN_REQUEST_ERROR }
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
