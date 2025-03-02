package ru.practicum.android.diploma.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.database.entity.VacancyEntity

@Dao
interface VacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyEntity)

    @Query("SELECT vacancyId FROM vacancies_table WHERE vacancyId == :vacancyId")
    suspend fun checkVacancyIsFavourite(vacancyId: String): String

    @Query("SELECT * FROM vacancies_table WHERE vacancyId == :vacancyId")
    suspend fun getVacancyData(vacancyId: String): VacancyEntity

    @Query("SELECT * FROM vacancies_table")
    fun getAllVacancies(): Flow<List<VacancyEntity>>

    @Delete()
    suspend fun removeFromFavourites(vacancy: VacancyEntity)
}
