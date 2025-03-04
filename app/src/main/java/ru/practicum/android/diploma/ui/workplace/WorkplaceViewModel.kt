package ru.practicum.android.diploma.ui.workplace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkplaceViewModel : ViewModel() {
    private val _countryName = MutableLiveData<String>()
    val countryName: LiveData<String> get() = _countryName

    private val _regionName = MutableLiveData<String>()
    val regionName: LiveData<String> get() = _regionName

    fun setCountryName(name: String) {
        _countryName.value = name
    }

    fun setRegionName(name: String) {
        _regionName.value = name
    }
}
