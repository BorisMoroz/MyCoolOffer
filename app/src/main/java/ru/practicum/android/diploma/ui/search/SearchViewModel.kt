package ru.practicum.android.diploma.ui.search

import android.util.Log
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

                                Log.i("ERROR", "I am here! 1")

                                /*val error = SearchTracksState.Error(result.message)
                                state.postValue(error)*/
                            }
                            is Resource.Success -> {

                                Log.i("SUCCESS", "I am here! 2")

                                /*val content = SearchTracksState.Content(result.data)
                                state.postValue(content)*/
                            }
                        }
                    }
            }

    }





}
