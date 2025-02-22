package ru.practicum.android.diploma.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyAdapter: RecyclerView.Adapter<VacancyViewHolder>() {

    var data: List<Vacancy> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_item, parent, false)
        return VacancyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(data[position])
    }

}
