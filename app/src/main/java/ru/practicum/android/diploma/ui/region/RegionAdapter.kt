package ru.practicum.android.diploma.ui.region

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region

class RegionAdapter(
    private val regions: List<Region>,
    private val listener: OnRegionClickListener
) :
    RecyclerView.Adapter<RegionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.country_or_region_item, parent, false)
        return RegionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return regions.size
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        holder.bind(regions[position])
        holder.itemView.setOnClickListener {
            listener.onRegionClick(regions[position])
        }
    }
}
