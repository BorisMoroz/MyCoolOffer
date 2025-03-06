package ru.practicum.android.diploma.domain.repository

interface FilterSettingsRepository {
    fun saveSettings(settings: Map<String, String>)
    fun getSettings(): Map<String, String>
    fun clearSettings()
}
