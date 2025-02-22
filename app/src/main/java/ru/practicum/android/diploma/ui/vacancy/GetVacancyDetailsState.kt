package ru.practicum.android.diploma.ui.vacancy

import ru.practicum.android.diploma.domain.models.VacancyDetails

sealed interface GetVacancyDetailsState {
    object Loading : GetVacancyDetailsState
    data class Error(val errorCode: Int) : GetVacancyDetailsState
    data class Content(val data: VacancyDetails) : GetVacancyDetailsState
}
