package ru.practicum.android.diploma.ui.favorites

interface FavouritesVacancyState {
    data object Empty : FavouritesVacancyState
    data object Success : FavouritesVacancyState
    data object Error : FavouritesVacancyState
}
