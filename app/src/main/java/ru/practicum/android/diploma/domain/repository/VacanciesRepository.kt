package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacancyDetails

interface VacanciesRepository {
    fun searchVacancies(
        text: String,
        page: Int,
        perPage: Int,
        area: Int?,
        industries: Int?,
        onlyWithSalary: Boolean
    ): Flow<Resource<Vacancies>>
    fun getVacancyDetails(vacancyId: String): Flow<Resource<VacancyDetails>>
}
