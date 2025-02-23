package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactor.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Resource

class SearchViewModel(val vacanciesInteractor: VacanciesInteractor) : ViewModel() {
    private var searchVacanciesState = MutableLiveData<SearchVacanciesState?>()

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
}
