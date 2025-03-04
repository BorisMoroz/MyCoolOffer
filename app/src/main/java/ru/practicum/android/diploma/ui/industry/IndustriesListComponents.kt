package ru.practicum.android.diploma.ui.industry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Industry

class IndustryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val industryName: TextView = itemView.findViewById(R.id.industryName)
    private val industryRadioButton: RadioButton = itemView.findViewById(R.id.industryRadioButton)

    fun bind(industry: Industry, selectedIndustry: Industry?/*IndustryWithMark*/) {
        industryName.text = industry.industryName
        if (selectedIndustry == null) {
            industryRadioButton.isChecked = false
        } else {
            industryRadioButton.isChecked = industry.industryId == selectedIndustry.industryId
        }
    }
}

class IndustryAdapter(
    private val industries: List<Industry>,
    val onChoosedIndustry: (Industry?) -> Unit
) :
    RecyclerView.Adapter<IndustryViewHolder>() {

    private var selectedIndustry: Industry? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.industry_item, parent, false)
        return IndustryViewHolder(view)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(industries[position], selectedIndustry)

        holder.itemView.setOnClickListener {
            selectedIndustry = industries[position]
            notifyDataSetChanged()
            onChoosedIndustry(selectedIndustry)
        }
    }

    override fun getItemCount(): Int {
        return industries.size
    }

    fun setSelectedIndutsry(selectedIndustry: Industry?) {
        this.selectedIndustry = selectedIndustry
    }
}
