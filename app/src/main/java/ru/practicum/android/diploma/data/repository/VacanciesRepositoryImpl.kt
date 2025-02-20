package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.EmployerDto
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.repository.VacanciesRepository

import ru.practicum.android.diploma.domain.models.Resource


class VacanciesRepositoryImpl(private val networkClient: NetworkClient) : VacanciesRepository {

    override fun searchVacancies(text: String): Flow<Resource<List<Vacancy>>> = flow {

        val response = networkClient.doRequest(VacancySearchRequest(text))

        if (response.resultCode == Response.OK) {



            val vacancies = (response as VacanciesSearchResponse).items.map {







                Vacancy(it.id, it.name, it.area?.name, it.employer?.name, it.employer?.logo_urls?.original,
                    it.salary?.from, it.salary?.to, it.salary?.currency)
            }



            /*val vacancies = (response as VacanciesSearchResponse).items.map {
                Vacancy(it.id, it.name, it.area.name, it.employer.name, it.employer.logo_urls.original,
                    it.salary.from, it.salary.to)
            }*/

            emit(Resource.Success(vacancies))

        } else {
            emit(Resource.Error("Произошла сетевая ошибка"))
        }
    }
}


/*
data class Vacancy(
    val vacancyId: String,
    val vacancyName: String,
    val area: String,
    val employer: String,
    val logoUrl : String,
    val salaryFrom: Int,
    val salaryTo: Int
)








class TracksRepositoryImpl(private val networkClient: NetworkClient, private val appDatabase: AppDatabase) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            val favoriteTracksIds = appDatabase.favoriteTrackDao().getTracksIds()

            val tracks = (response as TracksSearchResponse).results.map {
                Track(it.trackId, it.collectionName, it.releaseDate, it.primaryGenreName, it.country,
                    it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.previewUrl)
            }

            for(track in tracks) {
                if (track.trackId in favoriteTracksIds)
                    track.isFavorite = true
            }
            emit(Resource.Success(tracks))
        } else {
            emit(Resource.Error("Произошла сетевая ошибка"))
        }
    }
}*/
