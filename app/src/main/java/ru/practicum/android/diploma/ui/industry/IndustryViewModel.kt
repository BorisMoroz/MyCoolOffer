package ru.practicum.android.diploma.ui.industry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactor.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Resource

class IndustryViewModel(val vacanciesInteractor: VacanciesInteractor) : ViewModel() {
    private var industriesLoaded = false

    private var loadedIndustries = Industries(mutableListOf())
    private var filteredIndustries = Industries(mutableListOf())

    private var getIndustriesState = MutableLiveData<GetIndustriesState?>()

    fun getGetIndustriesState(): LiveData<GetIndustriesState?> = getIndustriesState

    fun searchIndustries(text: String) {
        getIndustriesState.postValue(GetIndustriesState.Loading)

        if (!industriesLoaded) {
            viewModelScope.launch {
                vacanciesInteractor
                    .getIndustries()
                    .collect { result ->
                        when (result) {
                            is Resource.Error -> {
                                val errorCode = GetIndustriesState.Error(result.errorCode)
                                getIndustriesState.postValue(errorCode)
                            }
                            is Resource.Success -> {
                                loadedIndustries = result.data

                                filterIndustries(text)

                                industriesLoaded = true

                                val content = GetIndustriesState.Content(filteredIndustries)
                                getIndustriesState.postValue(content)
                            }
                        }
                    }
            }
        } else {
            filterIndustries(text)

            val content = GetIndustriesState.Content(filteredIndustries)
            getIndustriesState.postValue(content)
        }
    }

    fun filterIndustries(text: String) {
        filteredIndustries.items.clear()

        if (text.isNotEmpty()) {
            for (industry in loadedIndustries.items) {
                if (industry.industryName.lowercase().contains(text.lowercase())) {
                    filteredIndustries.items.add(industry)
                }
            }
        } else {
            filteredIndustries.items.addAll(loadedIndustries.items)
        }
    }
}
