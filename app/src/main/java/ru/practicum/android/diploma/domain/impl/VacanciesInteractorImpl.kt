package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.interactor.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.domain.repository.VacanciesRepository

class VacanciesInteractorImpl(private val repository: VacanciesRepository) : VacanciesInteractor {
    override fun searchVacancies(text: String, page: Int, perPage: Int): Flow<Resource<Vacancies>> {
        return repository.searchVacancies(text, page, perPage)
    }
    override fun getVacancyDetails(vacancyId: String): Flow<Resource<VacancyDetails>> {
        return repository.getVacancyDetails(vacancyId)
    }
}
