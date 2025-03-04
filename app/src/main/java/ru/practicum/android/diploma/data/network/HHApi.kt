package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig.HH_ACCESS_TOKEN
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.responses.AreasResponse
import ru.practicum.android.diploma.data.dto.responses.VacanciesSearchResponse
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailsResponse

const val HHBASEURL = "https://api.hh.ru/"

const val APP_NAME = "MyCoolOffer"
const val EMAIL = "widebox@yandex.ru"

interface HHApi {
    @Headers("Authorization: Bearer ${HH_ACCESS_TOKEN}", "HH-User-Agent: $APP_NAME $EMAIL")
    @GET("/vacancies")
    suspend fun searchVacancies(
        @Query("text") text: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("area") area: Int?,
        @Query("industries") industries: Int?,
        @Query("only_with_salary") onlyWithSalary: Boolean,
        @Query("search_field") searchField: String = "name"
    ): VacanciesSearchResponse

    @Headers("Authorization: Bearer ${HH_ACCESS_TOKEN}", "HH-User-Agent: $APP_NAME $EMAIL")
    @GET("/vacancies/{vacancyId}")
    suspend fun getVacancyDetails(@Path("vacancyId") vacancyId: String): VacancyDetailsResponse

    @Headers("Authorization: Bearer ${HH_ACCESS_TOKEN}", "HH-User-Agent: $APP_NAME $EMAIL")
    @GET("/industries")
    suspend fun searchIndustries(): List<IndustryDto>

    @Headers("Authorization: Bearer ${HH_ACCESS_TOKEN}", "HH-User-Agent: $APP_NAME $EMAIL")
    @GET("/areas/{areaId}")
    suspend fun getAreas(@Path("areaId") areaId: String): AreasResponse

}
