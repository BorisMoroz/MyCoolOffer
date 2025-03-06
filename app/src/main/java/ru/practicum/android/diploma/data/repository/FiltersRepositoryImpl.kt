package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.requests.AreasRequest
import ru.practicum.android.diploma.data.dto.responses.AreasResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.repository.FiltersRepository
import ru.practicum.android.diploma.util.NETWORK_OK

class FiltersRepositoryImpl(private val networkClient: NetworkClient) : FiltersRepository {
    override fun getAreas(id: String): Flow<Resource<ArrayList<Area>>> = flow {
        val response = networkClient.doRequest(AreasRequest(id))
        if (response.resultCode == NETWORK_OK) {
            val areas = (response as AreasResponse).areas.map {
                Area(
                    it.id,
                    it.parentId,
                    it.name
                )
            }
            emit(Resource.Success(ArrayList(areas)))
        } else {
            emit(Resource.Error(response.resultCode))
        }
    }
}
