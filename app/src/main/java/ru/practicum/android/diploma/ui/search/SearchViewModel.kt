package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactor.SearchVacanciesInteractor
import ru.practicum.android.diploma.domain.models.Resource

class SearchViewModel(val searchVacanciesInteractor: SearchVacanciesInteractor) : ViewModel() {
    fun searchVacancies(query: String) {
        viewModelScope.launch {
            searchVacanciesInteractor
                .searchVacancies(query)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            //
                            //
                            // Здесь надо обновить LiveData
                            //
                            //
                        }
                        is Resource.Success -> {
                            //
                            //
                            // Здесь надо обновить LiveData
                            //
                            //
                        }
                    }
                }
        }
    }
}
