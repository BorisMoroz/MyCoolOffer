package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.interactor.SearchVacanciesInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.repository.VacanciesRepository

class SearchVacanciesInteractorImpl(private val repository: VacanciesRepository) : SearchVacanciesInteractor{

    override fun searchVacancies(text : String): Flow<Resource<List<Vacancy>>> {

        return repository.searchVacancies(text)
    }
}
