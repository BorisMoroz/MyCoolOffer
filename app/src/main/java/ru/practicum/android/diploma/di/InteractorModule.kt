package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.impl.SearchVacanciesInteractorImpl
import ru.practicum.android.diploma.domain.interactor.SearchVacanciesInteractor

val interactorModule = module {



    single<SearchVacanciesInteractor> {
        SearchVacanciesInteractorImpl(get())
    }





}
