package ru.practicum.android.diploma.ui.workplace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkplaceViewModel : ViewModel() {
    private val _countryId = MutableLiveData<String>()
    val countryId: LiveData<String> get() = _countryId

    private val _regionId = MutableLiveData<String>()
    val regionId: LiveData<String> get() = _regionId

    fun setCountryId(id: String) {
        _countryId.value = id
    }

    fun setRegionId(id: String) {
        _regionId.value = id
    }
}
