package ru.practicum.android.diploma.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactor.FavouriteVacanciesInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.State

class FavouritesViewModel(
    private val favouriteVacanciesInteractor: FavouriteVacanciesInteractor
): ViewModel() {

    private val vacancyListState = MutableLiveData<State.FavouriteVacancyList>()
    fun getVacancyListState(): LiveData<State.FavouriteVacancyList> = vacancyListState

    private val vacancyList = MutableLiveData<List<Vacancy>>()
    fun getVacancyList(): LiveData<List<Vacancy>> = vacancyList

    fun checkVacancyList() {
        viewModelScope.launch {
            favouriteVacanciesInteractor
                .getAllVacancies()
                .collect{ favouriteVacancies ->
                    if (favouriteVacancies.isEmpty()) {
                        vacancyListState.value = State.FavouriteVacancyList.EMPTY_LIST
                        vacancyList.value = favouriteVacancies.reversed()
                    } else {
                        vacancyListState.value = State.FavouriteVacancyList.SUCCESS
                        vacancyList.value = favouriteVacancies.reversed()
                    }
                }
        }
    }

}
