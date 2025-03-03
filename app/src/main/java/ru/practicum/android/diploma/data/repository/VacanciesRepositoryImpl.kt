package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.requests.VacancyDetailsRequest
import ru.practicum.android.diploma.data.dto.responses.VacanciesSearchResponse
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailsResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.SearchFilters
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.domain.repository.VacanciesRepository
import ru.practicum.android.diploma.util.NETWORK_OK

class VacanciesRepositoryImpl(private val networkClient: NetworkClient) : VacanciesRepository {
    override fun searchVacancies(params: SearchFilters): Flow<Resource<Vacancies>> = flow {
        val response =
            networkClient.doRequest(VacanciesSearchRequest(params))

        if (response.resultCode == NETWORK_OK) {
            val items = (response as VacanciesSearchResponse).items.map {
                Vacancy(
                    it.id,
                    it.name,
                    it.area?.name,
                    it.employer?.name,
                    it.employer?.logoUrls?.original,
                    it.salary?.from,
                    it.salary?.to,
                    it.salary?.currency
                )
            }
            val pages = response.pages
            val found = response.found
            val vacancies = Vacancies(items, pages, found)

            emit(Resource.Success(vacancies))
        } else {
            emit(Resource.Error(response.resultCode))
        }
    }

    override fun getVacancyDetails(vacancyId: String): Flow<Resource<VacancyDetails>> = flow {
        val response = networkClient.doRequest(VacancyDetailsRequest(vacancyId))

        if (response.resultCode == NETWORK_OK) {
            val vacancyDetailsResponse = response as VacancyDetailsResponse
            val vacancyDetails = VacancyDetails(
                vacancyDetailsResponse.id,
                vacancyDetailsResponse.name,
                vacancyDetailsResponse.salary?.from,
                vacancyDetailsResponse.salary?.to,
                vacancyDetailsResponse.salary?.currency,
                vacancyDetailsResponse.employer?.name,
                vacancyDetailsResponse.employer?.logoUrls?.original,
                vacancyDetailsResponse.area?.name,
                vacancyDetailsResponse.address?.raw,
                vacancyDetailsResponse.experience?.name,
                vacancyDetailsResponse.employment?.name,
                vacancyDetailsResponse.workFormat?.map { it?.name },
                vacancyDetailsResponse.description,
                vacancyDetailsResponse.keySkills?.map { it?.name }
            )
            emit(Resource.Success(vacancyDetails))
        } else {
            emit(Resource.Error(response.resultCode))
        }
    }
}
