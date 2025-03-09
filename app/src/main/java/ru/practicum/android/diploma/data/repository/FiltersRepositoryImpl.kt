package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.requests.AllAreasRequest
import ru.practicum.android.diploma.data.dto.requests.AreasRequest
import ru.practicum.android.diploma.data.dto.responses.AllAreasResponse
import ru.practicum.android.diploma.data.dto.responses.AreasResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.AllAreas
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.repository.FiltersRepository
import ru.practicum.android.diploma.util.NETWORK_OK

class FiltersRepositoryImpl(private val networkClient: NetworkClient) : FiltersRepository {
    override fun getAreas(id: String): Flow<Resource<ArrayList<Area>>> = flow {
        val response = networkClient.doRequest(AreasRequest(id))
        if (response.resultCode == NETWORK_OK) {
            val areasDto = (response as AreasResponse).areas
            val areas = if (id == ZERO_INDEX) createCountries(areasDto) else createAreas(areasDto)
            emit(Resource.Success(ArrayList(areas)))
        } else {
            emit(Resource.Error(response.resultCode))
        }
    }

    override fun getAllAreas(): Flow<Resource<AllAreas>> = flow {
        val response = networkClient.doRequest(AllAreasRequest())
        if (response.resultCode == NETWORK_OK) {
            val allAreasDto = (response as AllAreasResponse).allAreas
            val areas = createAllAreas(allAreasDto)
            emit(Resource.Success(AllAreas(areas)))
        } else {
            emit(Resource.Error(response.resultCode))
        }
    }

    private fun createAreas(areaDto: List<AreaDto>): List<Area> {
        val areas = mutableListOf<Area>()
        for (area in areaDto) {
            areas.add(
                Area(
                    id = area.id,
                    parentId = area.parentId,
                    name = area.name
                )
            )
            if (area.areas != null) {
                for (subArea in area.areas) {
                    areas.add(
                        Area(
                            id = subArea.id,
                            parentId = area.id,
                            name = subArea.name
                        )
                    )
                }
            }
        }
        return areas
    }

    private fun createCountries(areaDto: List<AreaDto>): List<Area> {
        val areas = mutableListOf<Area>()
        for (area in areaDto) {
            areas.add(
                Area(
                    id = area.id,
                    parentId = area.parentId,
                    name = area.name
                )
            )
        }
        return areas
    }

    private fun createAllAreas(allAreasDto: List<AreaDto>): MutableList<Area> {
        val areas = mutableListOf<Area>()
        for (areaDto in allAreasDto) {
            if (areaDto.areas != null) {
                for (subAreaDto in areaDto.areas) {
                    areas.add(
                        Area(
                            id = subAreaDto.id,
                            parentId = subAreaDto.parentId,
                            name = subAreaDto.name
                        )
                    )
                    checkSubAreaDtoAreas(subAreaDto.areas, areas, areaDto.id)
                }
            }
        }
        return areas
    }

    private fun checkSubAreaDtoAreas(subAreaDtoAreas: List<AreaDto>?, resultArea: MutableList<Area>, parentId: String) {
        if (subAreaDtoAreas != null) {
            for (lowLevelAreaDto in subAreaDtoAreas) {
                resultArea.add(
                    Area(
                        id = lowLevelAreaDto.id,
                        parentId = parentId,
                        name = lowLevelAreaDto.name
                    )
                )
            }
        }
    }

    private companion object {
        const val ZERO_INDEX = "0"
    }
}
