package ru.practicum.android.diploma.ui.country

import ru.practicum.android.diploma.domain.models.Area

interface CountryState {
    data object Loading : CountryState
    data class Content(val data: ArrayList<Area>) : CountryState
    data class Error(val errorCode: Int) : CountryState
}
