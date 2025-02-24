package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactor.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Resource

class SearchViewModel(val vacanciesInteractor: VacanciesInteractor) : ViewModel() {
    private var searchJob: Job? = null
    private var searchVacanciesState = MutableLiveData<SearchVacanciesState?>(SearchVacanciesState.Default)

    fun getSearchVacanciesState(): LiveData<SearchVacanciesState?> = searchVacanciesState

    fun searchVacancies(query: String, page: Int, perPage: Int) {
        searchVacanciesState.postValue(SearchVacanciesState.Loading)

        viewModelScope.launch {
            vacanciesInteractor
                .searchVacancies(query, page, perPage)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            val errorCode = SearchVacanciesState.Error(result.errorCode)
                            searchVacanciesState.postValue(errorCode)
                        }

                        is Resource.Success -> {
                            val content = SearchVacanciesState.Content(result.data)
                            searchVacanciesState.postValue(content)
                        }
                    }
                }
        }
    }

    fun searchDebounce(query: String, page: Int, perPage: Int) {
        if (query.isEmpty()) {
            searchJob?.cancel()
        } else {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                searchVacancies(query, page, perPage)
            }
        }
    }

    fun stopSearch() {
        searchJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
