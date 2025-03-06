package ru.practicum.android.diploma.domain.models

data class SearchFilters(
    val text: String,
    val page: Int,
    val perPage: Int,
    val area: String?,
    val industries: String?,
    val salary: String?,
    val onlyWithSalary: Boolean
)
