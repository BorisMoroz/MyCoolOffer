package ru.practicum.android.diploma.ui.industry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R

class IndustryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val industryName: TextView = itemView.findViewById(R.id.industryName)
    private val industryRadioButton: RadioButton = itemView.findViewById(R.id.industryRadioButton)

    fun bind(industry: IndustryWithMark) {
        industryName.text = industry.industry.industryName
        industryRadioButton.isChecked = industry.mark
    }
}

class IndustryAdapter(
    private val industries: List<IndustryWithMark>,
    val onChoosedIndustry: (Int) -> Unit
) :
    RecyclerView.Adapter<IndustryViewHolder>() {
    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.industry_item, parent, false)
        return IndustryViewHolder(view)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(industries[position])

        holder.itemView.setOnClickListener {
            if (selectedPosition != -1) {
                industries[selectedPosition].mark = false
            }
            selectedPosition = position
            industries[selectedPosition].mark = true

            notifyDataSetChanged()

            onChoosedIndustry(selectedPosition)
        }
    }

    override fun getItemCount(): Int {
        return industries.size
    }
}
