package ru.practicum.android.diploma.ui.search

import ru.practicum.android.diploma.domain.models.Vacancy

interface OnVacancyClickListener {
    fun onVacancyClick(vacancy: Vacancy)
}
