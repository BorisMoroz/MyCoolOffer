package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.data.database.entity.VacancyEntity
import ru.practicum.android.diploma.domain.models.Vacancy
import java.text.NumberFormat
import java.util.Locale

object Converter {

     private const val NO_SALARY = "Зарплата не указана"

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

    fun convertVacancyToVacancyEntity(vacancy: Vacancy): VacancyEntity {
        return VacancyEntity(
            vacancyId = vacancy.vacancyId,
            vacancyName = vacancy.vacancyName,
            area = vacancy.area,
            employer = vacancy.employer,
            logoUrl = vacancy.logoUrl,
            salaryFrom = vacancy.salaryFrom,
            salaryTo = vacancy.salaryTo,
            currency = vacancy.currency
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
