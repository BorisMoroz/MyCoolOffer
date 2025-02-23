package ru.practicum.android.diploma.ui.vacancy

import ru.practicum.android.diploma.domain.models.VacancyDetails

sealed interface VacancyDetailsState {
    data object Loading : VacancyDetailsState
    data class Content(val data: VacancyDetails) : VacancyDetailsState
    data class Error(val errorCode: Int) : VacancyDetailsState
}
