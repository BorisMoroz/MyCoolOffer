package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactor.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Resource

class VacancyViewModel(private val vacanciesInteractor: VacanciesInteractor) : ViewModel() {
    private var vacancyDetailsState = MutableLiveData<VacancyDetailsState?>()

    fun getVacancyDetailsState(): LiveData<VacancyDetailsState?> = vacancyDetailsState

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

    fun getSalaryText(salaryFrom: Int?, salaryTo: Int?, currency: String?): String {
        val salaryText = StringBuilder()
        if (salaryFrom != null) {
            salaryText.append("$FROM_TEXT$salaryFrom ")
        }
        if (salaryTo != null) {
            salaryText.append("$TO_TEXT$salaryTo ")
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
        return if (salaryText.isEmpty()) DEFAULT_SALARY_TEXT else salaryText.toString()
    }

    fun getSkillsText(skills: List<String?>): String {
        val skillsText = StringBuilder()
        for (skill in skills) {
            if (skill != null) {
                skillsText.append(KEY_SKILLS_NEW_LINE_TEXT).append(skill).append("\n")
            }
        }
        return skillsText.toString()
    }

    companion object {
        const val FROM_TEXT = "от "
        const val TO_TEXT = "до "
        const val DEFAULT_SALARY_TEXT = "Уровень дохода не указан"
        const val KEY_SKILLS_NEW_LINE_TEXT = "   •   "
    }
}
