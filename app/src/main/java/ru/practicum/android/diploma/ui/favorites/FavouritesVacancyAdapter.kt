package ru.practicum.android.diploma.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails

class FavouritesVacancyAdapter : RecyclerView.Adapter<FavouritesViewHolder>() {

    var onFavouriteVacancyClick: ((VacancyDetails) -> Unit)? = null
    var data: List<VacancyDetails> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_item, parent, false)
        return FavouritesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onFavouriteVacancyClick?.invoke(data[position])
        }
    }
}
