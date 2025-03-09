package ru.practicum.android.diploma.ui.industry

import ru.practicum.android.diploma.domain.models.Industries

sealed interface GetIndustriesState {
    object Loading : GetIndustriesState
    data class Error(val errorCode: Int) : GetIndustriesState
    data class Content(val data: Industries) : GetIndustriesState
}
