package ru.practicum.android.diploma.ui.favorites

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val vacancy: TextView = itemView.findViewById(R.id.vacancy)
    private val company: TextView = itemView.findViewById(R.id.company)
    private val salary: TextView = itemView.findViewById(R.id.salary)
    private val cover: ImageView = itemView.findViewById(R.id.cover)

    fun bind(item: Vacancy) {
        val itemSalary = item.salaryFrom.toString() + " " + item.salaryTo.toString()
        vacancy.text = item.vacancyName
        company.text = item.employer
        salary.text = itemSalary

        Glide.with(itemView)
            .load(item.logoUrl)
            .centerCrop()
//            .transform(RoundedCorners(Converter.dpToPx(TRACK_IMAGE_RADIUS)))
            .placeholder(R.drawable.img_job_placeholder)
            .into(cover)
    }

}
