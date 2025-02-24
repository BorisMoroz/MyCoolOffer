package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.database.dao.VacancyDao
import ru.practicum.android.diploma.data.database.entity.VacancyEntity
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.domain.repository.FavouriteVacanciesRepository
import ru.practicum.android.diploma.util.Converter

class FavouriteVacanciesRepositoryImpl(
    private val vacancyDao: VacancyDao,
    private val converter: Converter
) : FavouriteVacanciesRepository {

    override suspend fun insertVacancy(vacancy: VacancyDetails) {
        vacancyDao.insertVacancy(converter.convertVacancyToVacancyEntity(vacancy))
    }

    override suspend fun checkVacancyIsFavourite(vacancyId: String): String {
        return vacancyDao.checkVacancyIsFavourite(vacancyId)
    }

    override fun getAllVacancies(): Flow<List<VacancyDetails>> = flow {
        val tracks = vacancyDao.getAllVacancies()
        emit(convertFromVacancyEntity(tracks))
    }

    override suspend fun removeFromFavourites(vacancy: VacancyDetails) {
        vacancyDao.removeFromFavourites(converter.convertVacancyToVacancyEntity(vacancy))
    }

    private fun convertFromVacancyEntity(vacancyList: List<VacancyEntity>): List<VacancyDetails> {
        return vacancyList.map { vacancy -> converter.convertVacancyEntityToVacancy(vacancy) }
    }

}
