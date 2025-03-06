package ru.practicum.android.diploma.ui.filter

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.interactor.FilterSettingsInteractor

class FilterViewModel(
    private val filterSettingsInteractor: FilterSettingsInteractor
) : ViewModel() {

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
