package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.VacancyDto

data class VacanciesSearchResponse(val items: ArrayList<VacancyDto>, val pages: Int, val found: Int) : Response()
