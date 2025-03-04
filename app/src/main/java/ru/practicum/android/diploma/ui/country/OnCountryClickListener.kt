package ru.practicum.android.diploma.ui.country

import ru.practicum.android.diploma.domain.models.Country

interface OnCountryClickListener {
    fun onCountryClick(country: Country)
}
