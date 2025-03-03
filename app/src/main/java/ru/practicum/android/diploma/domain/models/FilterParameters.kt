package ru.practicum.android.diploma.domain.models

class FilterParameters(
    var countryId: String,
    var countryName: String,
    var areaId: String,
    var areaName: String,
    var industryId: String,
    var industryName: String,
    var salary: Int,
    var onlyWithSalary: Boolean
)
