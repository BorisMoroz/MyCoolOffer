package ru.practicum.android.diploma.domain.models

data class Vacancy(
    val vacancyId: String,
    val vacancyName: String,
    val area: String?,
    val employer: String?,
    val logoUrl : String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val currency : String?
)
