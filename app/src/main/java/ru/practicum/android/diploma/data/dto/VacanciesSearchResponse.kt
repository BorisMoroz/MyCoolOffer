package ru.practicum.android.diploma.data.dto

data class VacanciesSearchResponse(val items: ArrayList<VacancyDto>, val pages: Int, val found: Int) : Response()
