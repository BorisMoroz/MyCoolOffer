package ru.practicum.android.diploma.ui.favorites

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.Converter

class FavouritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val image: ImageView = itemView.findViewById(R.id.image)
    private val vacancyTitle: TextView = itemView.findViewById(R.id.vacancy)
    private val company: TextView = itemView.findViewById(R.id.company)
    private val salary: TextView = itemView.findViewById(R.id.salary)
    fun bind(model: VacancyDetails) {
        Glide.with(image)
            .load(model.logoUrl)
            .centerCrop()
            .placeholder(R.drawable.img_job_placeholder)
            .error(R.drawable.img_job_placeholder)
            .into(image)
        vacancyTitle.text = model.area?.let { Converter.formatVacancyName(model.vacancyName, it) }
        company.text = model.employer
        salary.text = Converter.formatSalary(model.salaryFrom, model.salaryTo, model.currency)
    }
}
