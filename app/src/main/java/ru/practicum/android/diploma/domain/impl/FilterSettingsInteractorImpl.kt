package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.interactor.FilterSettingsInteractor
import ru.practicum.android.diploma.domain.repository.FilterSettingsRepository

class FilterSettingsInteractorImpl(
    private val filterSettingsRepository: FilterSettingsRepository
) : FilterSettingsInteractor {

    override fun saveSettings(settings: Map<String, String>) {
        filterSettingsRepository.saveSettings(settings)
    }

    override fun getSettings(): Map<String, String> {
        return filterSettingsRepository.getSettings()
    }

    override fun clearSettings() {
        filterSettingsRepository.clearSettings()
    }

}
