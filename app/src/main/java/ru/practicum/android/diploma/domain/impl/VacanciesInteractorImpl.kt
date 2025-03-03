package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.interactor.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.SearchFilters
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.domain.repository.VacanciesRepository

class VacanciesInteractorImpl(private val repository: VacanciesRepository) : VacanciesInteractor {
    override fun searchVacancies(params: SearchFilters): Flow<Resource<Vacancies>> {
        return repository.searchVacancies(params)
    }

    override fun getVacancyDetails(vacancyId: String): Flow<Resource<VacancyDetails>> {
        return repository.getVacancyDetails(vacancyId)
    }
}
