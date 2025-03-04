package ru.practicum.android.diploma.domain.interactor

interface FilterSettingsInteractor {
    fun saveSettings(settings: Map<String, String>)
    fun getSettings(): Map<String, String>
    fun clearSettings()
}
