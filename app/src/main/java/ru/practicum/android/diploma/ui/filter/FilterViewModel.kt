package ru.practicum.android.diploma.ui.filter

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.interactor.FilterSettingsInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters

class FilterViewModel(
    private val filterSettingsInteractor: FilterSettingsInteractor
) : ViewModel() {
    private var originalFilterParameters: FilterParameters? = null

    fun saveOriginalFilterParameters(filterParameters: FilterParameters) {
        originalFilterParameters = filterParameters
    }

    fun restoreOriginalFilterParameters() = originalFilterParameters

    fun clearOriginalFilterParameters() {
        originalFilterParameters = null
    }

    fun saveFilterSettings(settings: Map<String, String>) {
        filterSettingsInteractor.saveSettings(settings)
    }

    fun getFilterSettings(): Map<String, String> {
        return filterSettingsInteractor.getSettings()
    }

    fun clearFilterSettings() {
        filterSettingsInteractor.clearSettings()
    }
}
