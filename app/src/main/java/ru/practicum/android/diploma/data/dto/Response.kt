package ru.practicum.android.diploma.data.dto

open class Response {
    var resultCode = OK

    companion object {
        const val NETWORK_CONNECTION_ERROR = -1
        const val OK = 200
        const val NETWORK_ERROR = 400
        const val NETWORK_CONNECTION_RETRIES = 3
        const val NETWORK_CONNECTION_DELAY = 500L
    }
}
