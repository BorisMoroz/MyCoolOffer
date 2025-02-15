package ru.practicum.android.diploma.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.database.entity.VacancyEntity

@Dao
interface VacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVacancy(vacancy: VacancyEntity)

    @Query("SELECT * FROM vacancies_table ORDER BY publishedAt DESC")
    fun getAllVacancies(): Flow<List<VacancyEntity>>

    @Query("SELECT * FROM vacancies_table WHERE id = :vacancyId")
    fun getVacancyById(vacancyId: String): VacancyEntity?
}
