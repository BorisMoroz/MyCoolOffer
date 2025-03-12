package ru.practicum.android.diploma.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.AllAreas
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Resource

interface FiltersInteractor {
    fun getAreas(id: String): Flow<Resource<ArrayList<Area>>>
    fun getAllAreas(): Flow<Resource<AllAreas>>
}
