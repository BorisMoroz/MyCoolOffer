package ru.practicum.android.diploma.util

import com.google.gson.Gson
import ru.practicum.android.diploma.data.database.entity.VacancyEntity
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import java.text.NumberFormat
import java.util.Locale

object Converter {

    private const val NO_SALARY = "Зарплата не указана"
    private val gson = Gson()

    fun formatSalary(salaryFrom: Int?, salaryTo: Int?, currency: String?): String {
        val currencySymbols = mapOf(
            "RUR" to "₽",
            "USD" to "$",
            "EUR" to "€"
        )

        val currencySymbol = currencySymbols[currency] ?: currency.orEmpty()
        val formatter = NumberFormat.getInstance(Locale.FRANCE)

        val formattedFrom = salaryFrom?.let { formatter.format(it) }
        val formattedTo = salaryTo?.let { formatter.format(it) }

        return when {
            salaryFrom == null && salaryTo == null -> NO_SALARY
            salaryFrom != null && salaryTo != null -> "От $formattedFrom до $formattedTo $currencySymbol"
            salaryFrom != null -> "От $formattedFrom $currencySymbol"
            salaryTo != null -> "До $formattedTo $currencySymbol"
            else -> NO_SALARY
        }
    }

    fun formatVacancyName(vacancyName: String, vacancyArea: String): String {
        return "$vacancyName, $vacancyArea"
    }

    fun convertVacancyDetailsToVacancyEntity(vacancyDetails: VacancyDetails): VacancyEntity {
        return VacancyEntity(
            vacancyId = vacancyDetails.vacancyId,
            vacancyName = vacancyDetails.vacancyName,
            area = vacancyDetails.area,
            employer = vacancyDetails.employer,
            logoUrl = vacancyDetails.logoUrl,
            salaryFrom = vacancyDetails.salaryFrom,
            salaryTo = vacancyDetails.salaryTo,
            currency = vacancyDetails.currency,
            address = vacancyDetails.address,
            experience = vacancyDetails.experience,
            employment = vacancyDetails.employment,
            workFormat = gson.toJson(vacancyDetails.workFormat),
            description = vacancyDetails.description,
            keySkills = gson.toJson(vacancyDetails.keySkills)
        )
    }

    fun convertVacancyEntityToVacancyDetails(vacancyEntity: VacancyEntity): VacancyDetails {
        return VacancyDetails(
            vacancyId = vacancyEntity.vacancyId,
            vacancyName = vacancyEntity.vacancyName,
            area = vacancyEntity.area,
            employer = vacancyEntity.employer,
            logoUrl = vacancyEntity.logoUrl,
            salaryFrom = vacancyEntity.salaryFrom,
            salaryTo = vacancyEntity.salaryTo,
            currency = vacancyEntity.currency,
            address = vacancyEntity.address,
            experience = vacancyEntity.experience,
            employment = vacancyEntity.employment,
            workFormat = gson.fromJson(vacancyEntity.workFormat, Array<String>::class.java).toList(),
            description = vacancyEntity.description,
            keySkills = gson.fromJson(vacancyEntity.keySkills, Array<String>::class.java).toList()
        )
    }

    fun convertVacancyEntityToVacancy(vacancyEntity: VacancyEntity): Vacancy {
        return Vacancy(
            vacancyId = vacancyEntity.vacancyId,
            vacancyName = vacancyEntity.vacancyName,
            area = vacancyEntity.area,
            employer = vacancyEntity.employer,
            logoUrl = vacancyEntity.logoUrl,
            salaryFrom = vacancyEntity.salaryFrom,
            salaryTo = vacancyEntity.salaryTo,
            currency = vacancyEntity.currency
        )
    }
}
