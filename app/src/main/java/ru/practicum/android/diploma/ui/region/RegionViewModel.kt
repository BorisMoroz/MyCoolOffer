package ru.practicum.android.diploma.ui.region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactor.FiltersInteractor
import ru.practicum.android.diploma.domain.models.Resource

class RegionViewModel(private val filtersInteractor: FiltersInteractor) : ViewModel() {
    private val regionState = MutableLiveData<RegionState>()
    fun getRegionState(): LiveData<RegionState> = regionState

    fun getRegions(id: String) {
        regionState.value = RegionState.Loading
        viewModelScope.launch {
            filtersInteractor.getAreas(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        regionState.value = RegionState.Content(result.data)
                    }
                    is Resource.Error -> {
                        regionState.value = RegionState.Error(result.errorCode)
                    }
                }
            }
        }
    }

    fun getAllRegions() {
        regionState.value = RegionState.Loading
        viewModelScope.launch {
            filtersInteractor.getAllAreas()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            regionState.value = RegionState.Content(ArrayList(result.data.items))
                        }
                        is Resource.Error -> {
                            regionState.value = RegionState.Error(result.errorCode)
                        }
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
