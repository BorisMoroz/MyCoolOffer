package ru.practicum.android.diploma.ui.country

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Country

class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.regionOrCountryName)
    fun bind(model: Country){
        name.text = model.countryName
    }
}
