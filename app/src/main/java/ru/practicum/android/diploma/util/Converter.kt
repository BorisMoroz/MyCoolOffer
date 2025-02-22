package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.data.database.entity.VacancyEntity
import ru.practicum.android.diploma.domain.models.Vacancy

object Converter {

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
