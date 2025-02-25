package ru.practicum.android.diploma.domain.models

data class VacancyDetails(
    val vacancyName: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val currency: String?,
    val employer: String?,
    val logoUrl: String?,
    val area: String?,
    val address: String?,
    val experience: String?,
    val employment: String?,
    val schedule: String?,
    val description: String?,
    val keySkills: List<String?>?
)
