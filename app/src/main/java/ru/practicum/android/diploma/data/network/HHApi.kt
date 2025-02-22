package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig.HH_ACCESS_TOKEN
import ru.practicum.android.diploma.data.dto.VacanciesSearchResponse

const val HHBASEURL = "https://api.hh.ru/"

const val APP_NAME = "MyCoolOffer"
const val EMAIL = "widebox@yandex.ru"

interface HHApi {
    @Headers("Authorization: Bearer ${HH_ACCESS_TOKEN}", "HH-User-Agent: $APP_NAME $EMAIL")
    @GET("/vacancies")
    suspend fun searchVacancies(@Query("text") text: String): VacanciesSearchResponse
}
