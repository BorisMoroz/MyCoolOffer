package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.interactor.FiltersInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.repository.FiltersRepository

class FiltersInteractorImpl(private val repository: FiltersRepository) : FiltersInteractor {
    override fun getAreas(id: String): Flow<Resource<ArrayList<Area>>> {
        return repository.getAreas(id)
    }
}
