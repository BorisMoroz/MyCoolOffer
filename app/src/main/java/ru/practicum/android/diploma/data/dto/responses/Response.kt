package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.util.NETWORK_OK

open class Response {
    var resultCode = NETWORK_OK

    companion object {
        const val NETWORK_CONNECTION_RETRIES = 3
        const val NETWORK_CONNECTION_DELAY = 500L
    }
}
