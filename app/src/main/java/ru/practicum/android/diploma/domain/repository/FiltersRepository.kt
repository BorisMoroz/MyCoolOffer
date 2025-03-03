package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Resource

interface FiltersRepository {
    fun getAreas(id: String): Flow<Resource<ArrayList<Area>>>
}
