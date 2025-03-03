package ru.practicum.android.diploma.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.domain.repository.FilterSettingsRepository

class FilterSettingsRepositoryImpl(
    private val gson: Gson,
    private val sharedPrefs: SharedPreferences
) : FilterSettingsRepository {

    override fun saveSettings(settings: Map<String, String>) {
        val currentSettings = getSettings()
        val updatedSettings = currentSettings + settings

        sharedPrefs.edit()
            .putString(FILTER_SETTINGS, gson.toJson(updatedSettings))
            .apply()
    }

    override fun getSettings(): Map<String, String> {
        val jsonSettings = sharedPrefs.getString(FILTER_SETTINGS, null)

        return jsonSettings?.let { json ->
            val type = object : TypeToken<Map<String, String>>() {}.type
            gson.fromJson(json, type)
        } ?: emptyMap()
    }

    override fun clearSettings() {
        sharedPrefs.edit().clear().apply()
    }

    private companion object {
        const val FILTER_SETTINGS = "filter_settings"
    }

}
