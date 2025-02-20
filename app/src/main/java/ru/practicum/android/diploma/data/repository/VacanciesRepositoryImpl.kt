package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.repository.VacanciesRepository
import ru.practicum.android.diploma.util.NETWORK_OK

class VacanciesRepositoryImpl(private val networkClient: NetworkClient) : VacanciesRepository {
    override fun searchVacancies(text: String): Flow<Resource<Vacancies>> /*Flow<Resource<List<Vacancy>>>*/ = flow {
        val response = networkClient.doRequest(VacancySearchRequest(text))

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
            val vacancies = Vacancies(items, pages)

            emit(Resource.Success(vacancies))
        } else {
            emit(Resource.Error(response.resultCode))
        }
    }
}
