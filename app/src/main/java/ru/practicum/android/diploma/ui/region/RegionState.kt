package ru.practicum.android.diploma.ui.region

import ru.practicum.android.diploma.domain.models.Area

interface RegionState {
    object Loading : RegionState
    data class Content(val areas: ArrayList<Area>) : RegionState
    data class Error(val errorCode: Int) : RegionState
}
