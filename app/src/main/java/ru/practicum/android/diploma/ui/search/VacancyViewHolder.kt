package ru.practicum.android.diploma.ui.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy
import java.text.NumberFormat
import java.util.Locale

class VacancyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val image: ImageView = itemView.findViewById(R.id.image)
    private val vacancyTitle: TextView = itemView.findViewById(R.id.vacancy)
    private val company: TextView = itemView.findViewById(R.id.company)
    private val salary: TextView = itemView.findViewById(R.id.salary)
    fun bind(model: Vacancy) {
        Glide.with(image)
            .load(model.logoUrl)
            .centerCrop()
            .placeholder(R.drawable.img_job_placeholder)
            .error(R.drawable.img_job_placeholder)
            .into(image)
        vacancyTitle.text = model.vacancyName
        company.text = model.employer
        salary.text = formatSalary(model.salaryFrom, model.salaryTo, model.currency)
    }


    private fun formatSalary(salaryFrom: Int?, salaryTo: Int?, currency: String?): String {
        val currencySymbols = mapOf(
            "RUR" to "₽",
            "USD" to "$",
            "EUR" to "€"
        )

        val currencySymbol = currencySymbols[currency] ?: currency.orEmpty()
        val formatter = NumberFormat.getInstance(Locale.FRANCE)

        val formattedFrom = salaryFrom?.let { formatter.format(it) }
        val formattedTo = salaryTo?.let { formatter.format(it) }

        return when {
            salaryFrom == null && salaryTo == null -> NO_SALARY
            salaryFrom != null && salaryTo != null -> "От $formattedFrom до $formattedTo $currencySymbol"
            salaryFrom != null -> "От $formattedFrom $currencySymbol"
            salaryTo != null -> "До $formattedTo $currencySymbol"
            else -> NO_SALARY
        }
    }
    companion object {
        const val NO_SALARY = "Зарплата не указана"
    }
}
