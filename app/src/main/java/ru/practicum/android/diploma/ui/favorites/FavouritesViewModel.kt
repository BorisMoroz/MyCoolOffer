package ru.practicum.android.diploma.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.util.State

class FavouritesViewModel: ViewModel() {

    private val vacancyListState = MutableLiveData<State.FavouriteVacancyList>()
    fun getVacancyListState(): LiveData<State.FavouriteVacancyList> = vacancyListState

    init {
        vacancyListState.value = State.FavouriteVacancyList.SUCCESS
    }

}
