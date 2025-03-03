package ru.practicum.android.diploma.domain.models

data class FilterParameters(
    val countryId: String,
    val countryName: String,
    val areaId: String,
    val areaName: String,
    val industryId: String,
    val industryName: String,
    val salary: Int,
    val onlyWithSalary: Boolean
)
