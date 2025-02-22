package ru.practicum.android.diploma.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacancyDetails

interface VacanciesInteractor {
    fun searchVacancies(text: String, page: Int, perPage: Int): Flow<Resource<Vacancies>>
    fun getVacancyDetails(vacancyId: String): Flow<Resource<VacancyDetails>>
}
