package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactor.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Resource

class VacancyViewModel(val vacanciesInteractor: VacanciesInteractor) : ViewModel() {
    private var getVacancyDetailsState = MutableLiveData<GetVacancyDetailsState?>()

    fun getVacancyDetailsState(): LiveData<GetVacancyDetailsState?> = getVacancyDetailsState

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
}
