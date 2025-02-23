package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancies

class VacancyAdapter(private val vacancies: Vacancies, private val listener: OnVacancyClickListener) :
    RecyclerView.Adapter<VacancyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.vacancy_item, parent, false)
        return VacancyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return vacancies.items.size
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancies.items[position])
        holder.itemView.setOnClickListener {
            listener.onVacancyClick(vacancies.items[position])
        }
    }
}
