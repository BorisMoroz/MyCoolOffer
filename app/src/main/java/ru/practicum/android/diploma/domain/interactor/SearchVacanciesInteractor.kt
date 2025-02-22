package ru.practicum.android.diploma.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancies

interface SearchVacanciesInteractor {
    fun searchVacancies(text: String): Flow<Resource<Vacancies>>
}
