package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.interactor.FavouriteVacanciesInteractor
import ru.practicum.android.diploma.domain.interactor.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacancyViewModel(
    private val vacanciesInteractor: VacanciesInteractor,
    private val favouriteVacanciesInteractor: FavouriteVacanciesInteractor
) : ViewModel() {
    private var vacancyDetailsState = MutableLiveData<VacancyDetailsState?>()
    fun getVacancyDetailsState(): LiveData<VacancyDetailsState?> = vacancyDetailsState

    private val isVacancyFavouriteState = MutableLiveData<Boolean>()
    fun getIsVacancyFavouriteState(): LiveData<Boolean> = isVacancyFavouriteState

    private val vacancyFromDb = MutableLiveData<VacancyDetails>()
    fun getVacancyFromDb(): LiveData<VacancyDetails> = vacancyFromDb

    fun getVacancyDetails(vacancyId: String) {
        vacancyDetailsState.postValue(VacancyDetailsState.Loading)

        viewModelScope.launch {
            vacanciesInteractor
                .getVacancyDetails(vacancyId)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            vacancyDetailsState.value = VacancyDetailsState.Error(result.errorCode)

                        }

                        is Resource.Success -> {
                            vacancyDetailsState.value = VacancyDetailsState.Content(result.data)
                        }
                    }
                }
        }
    }

    fun getSalaryText(
        salaryFrom: Int?,
        salaryFromText: String,
        salaryTo: Int?,
        salaryToText: String,
        currency: String?,
        defaultText: String
    ): String {
        val salaryText = StringBuilder()
        if (salaryFrom != null) {
            salaryText.append("$salaryFromText$salaryFrom ")
        }
        if (salaryTo != null) {
            salaryText.append("$salaryToText$salaryTo")
        }
        if (currency != null) {
            when (currency) {
                "RUR" -> salaryText.append("₽")
                "USD" -> salaryText.append("$")
                "EUR" -> salaryText.append("€")
                "AZN" -> salaryText.append("₼")
                "KZT" -> salaryText.append("₸")
                "BYR" -> salaryText.append("Br")
                "GEL" -> salaryText.append("₾")
                "KGS" -> salaryText.append("с")
                "UZS" -> salaryText.append("Soʻm")
                "UAH" -> salaryText.append("₴")
            }
        }
        return if (salaryText.isEmpty()) defaultText else salaryText.toString()
    }

    fun getSkillsText(skills: List<String?>, keySkillsNewLine: String): String {
        val skillsText = StringBuilder()
        for (skill in skills) {
            if (skill != null) {
                skillsText.append(keySkillsNewLine).append(skill).append("\n")
            }
        }
        return skillsText.toString()
    }

    fun addVacancyToFavourites(vacancy: VacancyDetails) {
        viewModelScope.launch {
            favouriteVacanciesInteractor.insertVacancy(vacancy)
        }
    }

    fun removeVacancyFromFavourites(vacancy: VacancyDetails) {
        viewModelScope.launch {
            favouriteVacanciesInteractor.removeFromFavourites(vacancy)
        }
    }

    fun checkVacancyInFavouriteList(vacancyId: String) {
        viewModelScope.launch {
            val verifiableId = favouriteVacanciesInteractor.checkVacancyIsFavourite(vacancyId)
            isVacancyFavouriteState.value = vacancyId == verifiableId
        }
    }

    fun getVacancyDetailsFromDb(vacancyId: String) {
        viewModelScope.launch {
            vacancyFromDb.value = favouriteVacanciesInteractor.getVacancyData(vacancyId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun getWorkFormatText(workFormat: List<String?>?, employment: String?): String {
        val workFormatText = StringBuilder()
        if (workFormat != null) {
            workFormatText.append(employment).append(", ")
            for (format in workFormat) {
                if (format != null) {
                    workFormatText.append(format).append(", ")
                }
            }
            workFormatText.delete(workFormatText.length - 2, workFormatText.length)
        } else {
            workFormatText.append(employment)
        }
        return workFormatText.toString()
    }
}
