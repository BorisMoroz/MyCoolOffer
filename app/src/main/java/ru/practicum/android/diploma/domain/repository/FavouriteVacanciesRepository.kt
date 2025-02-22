package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface FavouriteVacanciesRepository {
    suspend fun insertVacancy(vacancy: Vacancy)
    suspend fun checkVacancyIsFavourite(vacancyId: String): String
    fun getAllVacancies(): Flow<List<Vacancy>>
    suspend fun removeFromFavourites(vacancy: Vacancy)
}
