package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.impl.VacanciesInteractorImpl
import ru.practicum.android.diploma.domain.interactor.VacanciesInteractor

val interactorModule = module {
    single<VacanciesInteractor> {
        VacanciesInteractorImpl(get())
    }
}
