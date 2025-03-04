package ru.practicum.android.diploma.domain.models

data class SearchFilters(
    val text: String,
    val page: Int,
    val perPage: Int,
    val area: Int?,
    val industries: String?,
    val salary: Int?,
    val onlyWithSalary: Boolean
)
