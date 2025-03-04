package ru.practicum.android.diploma.ui.workplace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactor.FiltersInteractor
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.Resource

class WorkplaceViewModel(private val filtersInteractor: FiltersInteractor) : ViewModel() {
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

    fun getCountryById(id: String) {
        viewModelScope.launch {
            filtersInteractor.getAreas("0").collect { result ->
                when (result) {
                    is Resource.Success -> {
                        for (item in result.data) {
                            if (item.id == id) {
                                _country.postValue(Country(item.id, item.name))
                            }
                        }

                    }

                    is Resource.Error -> {
                    }
                }
            }
        }
    }
}
