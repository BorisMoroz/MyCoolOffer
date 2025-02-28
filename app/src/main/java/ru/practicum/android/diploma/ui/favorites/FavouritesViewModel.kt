package ru.practicum.android.diploma.ui.favorites

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactor.FavouriteVacanciesInteractor
import ru.practicum.android.diploma.domain.models.Vacancy

class FavouritesViewModel(
    private val favouriteVacanciesInteractor: FavouriteVacanciesInteractor
) : ViewModel() {

    private val vacancyListState = MutableLiveData<State.FavouriteVacancyList>()
    fun getVacancyListState(): LiveData<State.FavouriteVacancyList> = vacancyListState

    private val vacancyList = MutableLiveData<List<Vacancy>>()
    fun getVacancyList(): LiveData<List<Vacancy>> = vacancyList

    fun checkVacancyList() {
        viewModelScope.launch {
            try {
                favouriteVacanciesInteractor
                    .getAllVacancies()
                    .collect { favouriteVacancies ->
                        if (favouriteVacancies.isEmpty()) {
                            vacancyListState.value = State.FavouriteVacancyList.EMPTY_LIST
                            vacancyList.value = favouriteVacancies.reversed()
                        } else {
                            vacancyListState.value = State.FavouriteVacancyList.SUCCESS
                            vacancyList.value = favouriteVacancies.reversed()
                        }
                    }
            } catch (e: SQLiteException) {
                Log.d("SQLite", "Database exception: ${e.message}")
                vacancyListState.value = State.FavouriteVacancyList.ERROR
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}
