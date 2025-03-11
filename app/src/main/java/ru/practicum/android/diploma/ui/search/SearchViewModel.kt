package ru.practicum.android.diploma.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactor.FilterSettingsInteractor
import ru.practicum.android.diploma.domain.interactor.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.SearchFilters
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchViewModel(
    private val vacanciesInteractor: VacanciesInteractor,
    private val filterSettingsInteractor: FilterSettingsInteractor
) : ViewModel() {
    private var searchJob: Job? = null
    private var searchVacanciesState = MutableLiveData<SearchVacanciesState>(SearchVacanciesState.Default)
    var filterSettings: Map<String, String> = emptyMap()
    private var currentPage = 0
    private var maxPages = 1
    private var vacanciesList = mutableListOf<Vacancy>()
    private var isNextPageLoading = false
    private var latestSearchText: String = ""

    fun getVacanciesList() = vacanciesList

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
                val areaValue = if (filterSettings[AREA_ID].isNullOrEmpty()) {
                    if (filterSettings[COUNTRY_ID].isNullOrEmpty()) {
                        null
                    } else {
                        filterSettings[COUNTRY_ID]
                    }
                } else {
                    filterSettings[AREA_ID]
                }

                val industryValue =
                    if (filterSettings[INDUSTRY_ID].isNullOrEmpty()) null else filterSettings[INDUSTRY_ID]
                val salaryValue = if (filterSettings[SALARY].isNullOrEmpty()) null else filterSettings[SALARY]
                val onlyWithSalaryValue = filterSettings[ONLY_WITH_SALARY].toBoolean()

                vacanciesInteractor
                    .searchVacancies(
                        SearchFilters(
                            text = query,
                            page = currentPage,
                            perPage = ITEMS_PER_PAGE,
                            area = areaValue,
                            industries = industryValue,
                            salary = salaryValue,
                            onlyWithSalary = onlyWithSalaryValue
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
                if (latestSearchText != query) {
                    searchVacancies(query, true)
                    latestSearchText = query
                }
            }
        }
    }

    private fun isRefresh(refresh: Boolean) {
        if (refresh) {
            currentPage = 0
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

    fun getFilterSettings() {
        if (filterSettings != filterSettingsInteractor.getSettings()) {
            filterSettings = filterSettingsInteractor.getSettings()
            searchVacanciesState.postValue(SearchVacanciesState.GetFilterSettings(filterSettings))
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    private companion object {
        var found = -1
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val ITEMS_PER_PAGE = 20
        const val AREA_ID = "areaId"
        const val COUNTRY_ID = "countryId"
        const val INDUSTRY_ID = "industryId"
        const val SALARY = "salary"
        const val ONLY_WITH_SALARY = "onlyWithSalary"
    }
}
