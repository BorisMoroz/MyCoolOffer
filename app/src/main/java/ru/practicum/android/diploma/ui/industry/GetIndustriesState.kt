package ru.practicum.android.diploma.ui.industry

import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Vacancies

sealed interface GetIndustriesState {
    /*data object Default : SearchVacanciesState
    data object Loading : SearchVacanciesState */
    data class Error(val errorCode: Int) : GetIndustriesState
    data class Content(val data: Industries) : GetIndustriesState
}
