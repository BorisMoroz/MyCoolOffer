package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.impl.FavouriteVacanciesInteractorImpl
import ru.practicum.android.diploma.domain.impl.FilterSettingsInteractorImpl
import ru.practicum.android.diploma.domain.impl.FiltersInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacanciesInteractorImpl
import ru.practicum.android.diploma.domain.interactor.FavouriteVacanciesInteractor
import ru.practicum.android.diploma.domain.interactor.FilterSettingsInteractor
import ru.practicum.android.diploma.domain.interactor.FiltersInteractor
import ru.practicum.android.diploma.domain.interactor.VacanciesInteractor

val interactorModule = module {
    single<VacanciesInteractor> {
        VacanciesInteractorImpl(get())
    }

    factory<FavouriteVacanciesInteractor> {
        FavouriteVacanciesInteractorImpl(
            repository = get()
        )
    }

    single<FilterSettingsInteractor> {
        FilterSettingsInteractorImpl(get())
    }
    single<FiltersInteractor> { FiltersInteractorImpl(get()) }

}
