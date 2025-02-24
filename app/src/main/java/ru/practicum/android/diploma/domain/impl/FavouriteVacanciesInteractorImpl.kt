package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.interactor.FavouriteVacanciesInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.domain.repository.FavouriteVacanciesRepository

class FavouriteVacanciesInteractorImpl(
    private val repository: FavouriteVacanciesRepository
) : FavouriteVacanciesInteractor {

    override suspend fun insertVacancy(vacancy: VacancyDetails) {
        repository.insertVacancy(vacancy)
    }

    override suspend fun checkVacancyIsFavourite(vacancyId: String): String {
        return repository.checkVacancyIsFavourite(vacancyId)
    }

    override fun getAllVacancies(): Flow<List<Vacancy>> {
        return repository.getAllVacancies()
    }

    override suspend fun removeFromFavourites(vacancy: VacancyDetails) {
        repository.removeFromFavourites(vacancy)
    }
}
