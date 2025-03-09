package ru.practicum.android.diploma.ui.country

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactor.FiltersInteractor
import ru.practicum.android.diploma.domain.models.Resource

class CountryViewModel(private val filtersInteractor: FiltersInteractor) : ViewModel() {
    private val countryState = MutableLiveData<CountryState>()
    fun getCountryState(): LiveData<CountryState> = countryState

    fun getCountries() {
        countryState.value = CountryState.Loading
        viewModelScope.launch {
            filtersInteractor.getAreas(ZERO_INDEX).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        countryState.value = CountryState.Content(result.data)
                    }
                    is Resource.Error -> {
                        countryState.value = CountryState.Error(result.errorCode)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    private companion object {
        const val ZERO_INDEX = "0"
    }
}
