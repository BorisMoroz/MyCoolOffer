package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.repository.FavouriteVacanciesRepositoryImpl
import ru.practicum.android.diploma.data.repository.FilterSettingsRepositoryImpl
import ru.practicum.android.diploma.data.repository.FiltersRepositoryImpl
import ru.practicum.android.diploma.data.repository.VacanciesRepositoryImpl
import ru.practicum.android.diploma.domain.repository.FavouriteVacanciesRepository
import ru.practicum.android.diploma.domain.repository.FilterSettingsRepository
import ru.practicum.android.diploma.domain.repository.FiltersRepository
import ru.practicum.android.diploma.domain.repository.VacanciesRepository

val repositoryModule = module {
    single<VacanciesRepository> {
        VacanciesRepositoryImpl(get())
    }

    single<FavouriteVacanciesRepository> {
        FavouriteVacanciesRepositoryImpl(
            vacancyDao = get(),
            converter = get()
        )
    }

    single<FilterSettingsRepository> {
        FilterSettingsRepositoryImpl(get(), get())
    }

    single<FiltersRepository> {
        FiltersRepositoryImpl(get())
    }

}
