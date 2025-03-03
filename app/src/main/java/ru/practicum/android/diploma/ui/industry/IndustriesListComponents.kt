package ru.practicum.android.diploma.ui.industry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Industry


//private var selectedIndustry: Industry? = null


//private var selectedIndustry = Industry("","")

//private var selectedIndustry = IndustryWithMark(Industry("",""), false)

class IndustryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val industryName: TextView = itemView.findViewById(R.id.industryName)
    private val industryRadioButton: RadioButton = itemView.findViewById(R.id.industryRadioButton)

    fun bind(industry: Industry, selectedIndustry: Industry?/*IndustryWithMark*/) {

        industryName.text = industry.industryName

        if (selectedIndustry == null) {

            industryRadioButton.isChecked = false
        } else {

            industryRadioButton.isChecked = industry.industryId == selectedIndustry!!.industryId

        }


        //industryRadioButton.isChecked = (industry.industryId == selectedIndustry.industryId) ?: false


        //industryRadioButton.isChecked = industry != null  //industry.industryId == selectedIndustry.industryId


        /*industryName.text = industry.industry.industryName

        industryRadioButton.isChecked = industry.industry.industryId == selectedIndustry.industry.industryId*/

        // industryRadioButton.isChecked = industry.mark
    }
}

class IndustryAdapter(
    private val industries: List<Industry>,
    //private val industries: List<IndustryWithMark>,
    val onChoosedIndustry: (Industry?) -> Unit
) :
    RecyclerView.Adapter<IndustryViewHolder>() {


    private var selectedIndustry: Industry? = null


    //var selectedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.industry_item, parent, false)
        return IndustryViewHolder(view)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(industries[position], selectedIndustry)

        holder.itemView.setOnClickListener {
            /*if (selectedPosition != -1) {
                industries[selectedPosition].mark = false
            }
            selectedPosition = position
            industries[selectedPosition].mark = true */



            selectedIndustry = industries[position]

            //selectedIndustry = industries[selectedPosition]


            notifyDataSetChanged()

            onChoosedIndustry(selectedIndustry)
        }
    }

    override fun getItemCount(): Int {
        return industries.size
    }
}
