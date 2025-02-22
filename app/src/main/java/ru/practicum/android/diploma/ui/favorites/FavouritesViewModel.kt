package ru.practicum.android.diploma.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.util.State

class FavouritesViewModel: ViewModel() {

    private val vacancyListStatus = MutableLiveData<State.FavouriteVacancyList>()
    fun getVacancyListStatus(): LiveData<State.FavouriteVacancyList> = vacancyListStatus

    init {
        vacancyListStatus.value = State.FavouriteVacancyList.EMPTY
    }

}
