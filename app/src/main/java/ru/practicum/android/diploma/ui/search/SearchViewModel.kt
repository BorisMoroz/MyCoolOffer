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
import ru.practicum.android.diploma.domain.models.SearchFilters
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchViewModel(
    private val vacanciesInteractor: VacanciesInteractor
) : ViewModel() {
    private var searchJob: Job? = null
    private var searchVacanciesState = MutableLiveData<SearchVacanciesState>(SearchVacanciesState.Default)

    private var currentPage = 1
    private var maxPages = 1
    private var vacanciesList = mutableListOf<Vacancy>()
    private var isNextPageLoading = false

    fun getSearchVacanciesState(): LiveData<SearchVacanciesState> = searchVacanciesState

    fun searchVacancies(query: String, refresh: Boolean = false) {
        if (!isNextPageLoading && query.isNotEmpty()) {
            isRefresh(refresh)

            checkCurrentPageAndSearchVacancies(query)
        }
    }

    private fun checkCurrentPageAndSearchVacancies(query: String) {
        if (currentPage <= maxPages) {
            searchVacanciesState.postValue(SearchVacanciesState.Loading)
            isNextPageLoading = true

            viewModelScope.launch {
                vacanciesInteractor
                    .searchVacancies(
                        SearchFilters(
                            text = query,
                            page = currentPage,
                            perPage = ITEMS_PER_PAGE,
                            area = null,
                            industries = null,
                            onlyWithSalary = false
                        )
                    )
                    .collect { result ->
                        when (result) {
                            is Resource.Error -> {
                                val errorCode = SearchVacanciesState.Error(result.errorCode)
                                searchVacanciesState.postValue(errorCode)
                                isNextPageLoading = false
                            }

                            is Resource.Success -> {
                                val response = result.data
                                found = if (found == -1) response.found else found
                                maxPages = response.pages
                                vacanciesList.addAll(response.items)
                                currentPage++

                                val content = SearchVacanciesState.Content(
                                    Vacancies(
                                        vacanciesList,
                                        currentPage,
                                        found
                                    )
                                )
                                searchVacanciesState.postValue(content)
                                isNextPageLoading = false
                            }
                        }
                    }
            }
        }
    }

    fun searchDebounce(query: String) {
        if (query.isEmpty()) {
            searchJob?.cancel()
        } else {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                searchVacancies(query, true)
            }
        }
    }

    private fun isRefresh(refresh: Boolean) {
        if (refresh) {
            currentPage = 1
            maxPages = 1
            found = -1
            vacanciesList.clear()
        }
    }

    fun stopSearch() {
        searchJob?.cancel()
    }

    fun resetState() {
        searchVacanciesState.value = SearchVacanciesState.Default
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    companion object {
        private var found = -1
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val ITEMS_PER_PAGE = 20
    }
}
