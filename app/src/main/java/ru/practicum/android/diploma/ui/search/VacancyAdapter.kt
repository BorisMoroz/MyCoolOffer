package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyAdapter(
    private val listener: OnVacancyClickListener,
    private val coroutineScope: CoroutineScope
) : RecyclerView.Adapter<VacancyViewHolder>() {

    private var vacancies = mutableListOf<Vacancy>()
    private var isClickAllowed = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.vacancy_item, parent, false)
        return VacancyViewHolder(view)
    }

    fun updateVacancies(newVacancies: List<Vacancy>) {
        val oldVacancies = vacancies
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldVacancies.size
            }

            override fun getNewListSize(): Int {
                return newVacancies.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldVacancies[oldItemPosition].vacancyId == newVacancies[newItemPosition].vacancyId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldVacancies[oldItemPosition] == newVacancies[newItemPosition]
            }
        })
        vacancies = newVacancies.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    fun clearVacancies() {
        vacancies.clear()
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancies[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                listener.onVacancyClick(vacancies[position])
            }
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            coroutineScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}
