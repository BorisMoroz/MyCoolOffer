package ru.practicum.android.diploma.data.dto.requests

data class VacanciesSearchRequest(
    val text: String,
    val page: Int,
    val perPage: Int,
    val area: Int?,
    val industries: Int?,
    val onlyWithSalary: Boolean
)
