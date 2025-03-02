package ru.practicum.android.diploma.ui.industry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactor.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Resource

class IndustryViewModel(val vacanciesInteractor: VacanciesInteractor) : ViewModel() {

    private var industriesLoaded = false

    private var getIndustriesState = MutableLiveData<GetIndustriesState?>()

    fun getGetIndustriesState(): LiveData<GetIndustriesState?> = getIndustriesState

    fun getIndustries() {
        viewModelScope.launch {
            vacanciesInteractor
                .getIndustries()
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            val errorCode = GetIndustriesState.Error(result.errorCode)
                            getIndustriesState.postValue(errorCode)
                        }
                        is Resource.Success -> {
                            val content = GetIndustriesState.Content(result.data)
                            getIndustriesState.postValue(content)
                        }
                    }
                }
        }
    }


    fun searchIndustries(text: String){








    }



}
