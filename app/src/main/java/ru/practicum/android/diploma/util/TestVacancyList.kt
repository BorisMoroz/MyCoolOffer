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
        vacancyId = "117274782",
        vacancyName = "Административный Ассистент в Посольство Бразилии в Астане",
        area = "Астана",
        employer = "Посольство Федеративной Республики Бразилия в Астане",
        logoUrl = "https://img.hhcdn.ru/employer-logo-original/103333.jpg",
        salaryFrom = null,
        salaryTo = null,
        currency = null
    )

    private val testVacancyThree = Vacancy(
        vacancyId = "117560796",
        vacancyName = "Кассир в Корейское кафе",
        area = "Москва",
        employer = "Kono",
        logoUrl = null,
        salaryFrom = null,
        salaryTo = 80000,
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
