package ru.practicum.android.diploma.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancies_table")
data class VacancyEntity(
    @PrimaryKey
    val vacancyId: String,
    val vacancyName: String,
    val area: String?,
    val employer: String?,
    val logoUrl: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val currency: String?
)
