package ru.practicum.android.diploma.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancies_table")
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val companyName: String,
    val location: String,
    val salary: String?,
    val description: String,
    val publishedAt: String
)
