package ru.practicum.android.diploma.ui.workplace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region

class WorkplaceViewModel : ViewModel() {
    private val _country = MutableLiveData<Country>()
    val country: LiveData<Country> get() = _country

    private val _region = MutableLiveData<Region>()
    val region: LiveData<Region> get() = _region

    fun setCountry(country: Country) {
        _country.value = country
    }

    fun setRegion(region: Region) {
        _region.value = region
    }
}
