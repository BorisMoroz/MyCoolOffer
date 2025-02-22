package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactor.FavouriteVacanciesInteractor
import ru.practicum.android.diploma.domain.interactor.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyViewModel(
    private val vacanciesInteractor: VacanciesInteractor,
    private val favouriteVacanciesInteractor: FavouriteVacanciesInteractor

    ) : ViewModel() {
    private var getVacancyDetailsState = MutableLiveData<GetVacancyDetailsState?>()

    fun getVacancyDetailsState(): LiveData<GetVacancyDetailsState?> = getVacancyDetailsState

    private val isVacancyFavourite = MutableLiveData<Boolean>()
    fun getIsVacancyFavourite(): LiveData<Boolean> = isVacancyFavourite

    fun getVacancyDetails(vacancyId: String) {
        getVacancyDetailsState.postValue(GetVacancyDetailsState.Loading)

        viewModelScope.launch {
            vacanciesInteractor
                .getVacancyDetails(vacancyId)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            val errorCode = GetVacancyDetailsState.Error(result.errorCode)
                            getVacancyDetailsState.postValue(errorCode)
                        }
                        is Resource.Success -> {
                            val content = GetVacancyDetailsState.Content(result.data)
                            getVacancyDetailsState.postValue(content)
                        }
                    }
                }
        }
    }

    fun addVacancyToFavourites(vacancy: Vacancy) {
        viewModelScope.launch {
            favouriteVacanciesInteractor
                .insertVacancy(vacancy)
        }
    }

    fun removeVacancyFromFavourites(vacancy: Vacancy) {
        viewModelScope.launch {
            favouriteVacanciesInteractor
                .removeFromFavourites(vacancy)
        }
    }

    fun checkVacancyInFavouriteList(vacancy: Vacancy) {
        viewModelScope.launch {
            val checkedId = favouriteVacanciesInteractor.checkVacancyIsFavourite(vacancy.vacancyId)
            isVacancyFavourite.value = checkedId == vacancy.vacancyId
        }
    }
}
