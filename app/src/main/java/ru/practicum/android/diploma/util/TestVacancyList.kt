package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.domain.models.Vacancy

class TestVacancyList {

    private val testVacancyOne = Vacancy(
        vacancyId = "117524853",
        vacancyName = "Семейный водитель",
        area = "Алматы",
        employer = "Doctor-Stom",
        logoUrl = "https://img.hhcdn.ru/employer-logo-original/786151.jpeg",
        salaryFrom = 200000,
        salaryTo = 300000,
        currency = "RUR"
    )

    private val testVacancyTwo = Vacancy(
        vacancyId = "117524853",
        vacancyName = "Семейный водитель",
        area = "Алматы",
        employer = "Doctor-Stom",
        logoUrl = "https://img.hhcdn.ru/employer-logo-original/786151.jpeg",
        salaryFrom = 200000,
        salaryTo = 300000,
        currency = "RUR"
    )

    private val testVacancyThree = Vacancy(
        vacancyId = "117524853",
        vacancyName = "Семейный водитель",
        area = "Алматы",
        employer = "Doctor-Stom",
        logoUrl = "https://img.hhcdn.ru/employer-logo-original/786151.jpeg",
        salaryFrom = 200000,
        salaryTo = 300000,
        currency = "RUR"
    )

    fun getTestVacancyOne(): Vacancy {
        return testVacancyOne
    }

    fun getTestVacancyTwo(): Vacancy {
        return testVacancyTwo
    }

    fun getTestVacancyThree(): Vacancy {
        return testVacancyThree
    }
}
