package ru.practicum.android.diploma.ui.country

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region

class AreaConverter {
    fun mapToCountry(area: Area): Country {
        return Country(
            area.id,
            area.name
        )
    }

    fun mapToRegion(area: Area): Region {
        return Region(
            area.id,
            area.name
        )
    }
}
