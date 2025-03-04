package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.country.CountryViewModel
import ru.practicum.android.diploma.ui.favorites.FavouritesViewModel
import ru.practicum.android.diploma.ui.filter.FilterViewModel
import ru.practicum.android.diploma.ui.industry.IndustryViewModel
import ru.practicum.android.diploma.ui.region.RegionViewModel
import ru.practicum.android.diploma.ui.search.SearchViewModel
import ru.practicum.android.diploma.ui.vacancy.VacancyViewModel
import ru.practicum.android.diploma.ui.workplace.WorkplaceViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        VacancyViewModel(
            vacanciesInteractor = get(),
            favouriteVacanciesInteractor = get()
        )
    }

    viewModel {
        IndustryViewModel(
            vacanciesInteractor = get()
        )
    }

    viewModel<FavouritesViewModel> {
        FavouritesViewModel(
            favouriteVacanciesInteractor = get()
        )
    }

    viewModel<WorkplaceViewModel> {
        WorkplaceViewModel()
    }

    viewModel {
        FilterViewModel(get())
    }

    viewModel {
        CountryViewModel(get())
    }

    viewModel {
        RegionViewModel(get())
    }

}
