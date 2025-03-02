package ru.practicum.android.diploma.ui.filter

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.domain.models.FilterParameters

class FilterFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private lateinit var inputMethod: InputMethodManager

    private lateinit var currentFilterParameters: FilterParameters

    var SalaryChanged = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        currentFilterParameters = getCurrentFilterParameters()

        inputMethod = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (currentFilterParameters.areaId.isEmpty()){
            binding.workplaceLayout1.visibility = View.VISIBLE
            binding.workplaceLayout2.visibility = View.INVISIBLE
        }else {
            binding.workplaceLayout1.visibility = View.INVISIBLE
            binding.workplaceLayout2.visibility = View.VISIBLE

            binding.workplaceName.text = currentFilterParameters.countryName

            if(currentFilterParameters.countryId != currentFilterParameters.areaId){
                binding.workplaceName.text = currentFilterParameters.countryName + ", " + currentFilterParameters.areaName
            }
        }

        if (currentFilterParameters.industryId.isEmpty()){
            binding.industryLayout1.visibility = View.VISIBLE
            binding.industryLayout2.visibility = View.INVISIBLE
        }else {
            binding.industryLayout1.visibility = View.INVISIBLE
            binding.industryLayout2.visibility = View.VISIBLE

            binding.industryName.text = currentFilterParameters.industryName
        }

        if(currentFilterParameters.salary != 0){
            binding.salaryEdittext.setText(currentFilterParameters.salary.toString())
            val  color = ContextCompat.getColor(requireContext(), R.color.yp_black);
            binding.salaryTitle.setTextColor(color)
        }

        binding.salaryCheckBox.isChecked = currentFilterParameters.onlyWithSalary

        if(isFilterParametersNotEmpty(currentFilterParameters)){
            binding.buttonReset.visibility = View.VISIBLE
        }

        val inputTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                SalaryChanged = true
                binding.buttonApply.isVisible = true
            }
        }

        binding.salaryEdittext.addTextChangedListener(inputTextWatcher)

        binding.salaryEdittext.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus && (view as EditText).text.isNotEmpty()){
                binding.clearButton.isVisible = true
            } else{
                binding.clearButton.isVisible = false
            }

            if(hasFocus){
                val  color = ContextCompat.getColor(requireContext(), R.color.yp_blue);
                binding.salaryTitle.setTextColor(color)
            } else {
                if(binding.salaryEdittext.text.isNotEmpty()) {
                    val  color = ContextCompat.getColor(requireContext(), R.color.yp_black);
                    binding.salaryTitle.setTextColor(color)
                }
               else {
                    val  color = ContextCompat.getColor(requireContext(), R.color.yp_gray);
                    binding.salaryTitle.setTextColor(color)
               }
               updatesSalary()
            }
        }

        binding.salaryLayout.setOnClickListener {
            binding.salaryEdittext.requestFocus()
        }

        binding.salaryEdittext.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_NEXT){
                inputMethod.hideSoftInputFromWindow(binding.salaryEdittext.windowToken, 0)

                updatesSalary()
            }
            false
        }

        binding.workplaceLayout1.setOnClickListener {
            inputMethod.hideSoftInputFromWindow(binding.salaryEdittext.windowToken, 0)
            binding.salaryEdittext.clearFocus()
        }

        binding.industryLayout1.setOnClickListener {
            inputMethod.hideSoftInputFromWindow(binding.salaryEdittext.windowToken, 0)
            binding.salaryEdittext.clearFocus()
        }

        binding.clearButton.setOnClickListener {
            binding.salaryEdittext.setText("")
        }

        binding.salaryCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            with(currentFilterParameters){
                onlyWithSalary = isChecked
            }

            saveCurrentFilterParameters()
            updateButtons()

            inputMethod.hideSoftInputFromWindow(binding.salaryEdittext.windowToken, 0)
            binding.salaryEdittext.clearFocus()
        }

        binding.workplaceButtonReset.setOnClickListener {
            binding.workplaceLayout1.visibility = View.VISIBLE
            binding.workplaceLayout2.visibility = View.INVISIBLE

            with(currentFilterParameters){
                countryName = ""
                countryId = ""
                areaName = ""
                areaId = ""
            }

            saveCurrentFilterParameters()
            updateButtons()

            inputMethod.hideSoftInputFromWindow(binding.salaryEdittext.windowToken, 0)
            binding.salaryEdittext.clearFocus()
        }

        binding.industryButtonReset.setOnClickListener {
            binding.industryLayout1.visibility = View.VISIBLE
            binding.industryLayout2.visibility = View.INVISIBLE

            with(currentFilterParameters){
                industryName = ""
                industryId = ""
            }

            saveCurrentFilterParameters()
            updateButtons()

            inputMethod.hideSoftInputFromWindow(binding.salaryEdittext.windowToken, 0)
            binding.salaryEdittext.clearFocus()
        }



        binding.buttonWorkplaceForward.setOnClickListener {



        }

        binding.buttonIndustryForward.setOnClickListener {


            findNavController().navigate(R.id.action_filterFragment_to_industryFragment)



        }



        binding.buttonReset.setOnClickListener {
            binding.workplaceLayout1.visibility = View.VISIBLE
            binding.workplaceLayout2.visibility = View.INVISIBLE
            binding.industryLayout1.visibility = View.VISIBLE
            binding.industryLayout2.visibility = View.INVISIBLE

            binding.salaryEdittext.setText("")
            binding.salaryEdittext.clearFocus()

            binding.salaryCheckBox.isChecked = false

            binding.buttonReset.visibility = View.INVISIBLE

            clearCurrentFilterParameters()
            saveCurrentFilterParameters()
        }

        binding.buttonApply.setOnClickListener {
            findNavController().navigateUp()
       }
    }

    fun updatesSalary(){
        if(SalaryChanged){
            with(currentFilterParameters){
                if(binding.salaryEdittext.text.isNotEmpty()){
                    salary = binding.salaryEdittext.text.toString().toInt()
                } else {
                    salary = 0
                }
            }
            saveCurrentFilterParameters()
            updateButtons()
            SalaryChanged = false
        }
    }

    fun updateButtons(){
        if(isFilterParametersNotEmpty(currentFilterParameters)){
            binding.buttonReset.visibility = View.VISIBLE
        } else {
            binding.buttonReset.visibility = View.INVISIBLE
        }
        binding.buttonApply.visibility = View.VISIBLE
    }

    fun getCurrentFilterParameters(): FilterParameters{
        return FilterParameters(
            countryId = "1000",
            countryName = "Россия",
            areaId = "2000",
            areaName = "Москва",
            industryId = "3000",
            industryName = "Торговля",
            salary = 100000,
            onlyWithSalary = true
        )
    }

    fun saveCurrentFilterParameters(){


   }

   fun clearCurrentFilterParameters(){
       with(currentFilterParameters){
           countryName = ""
           countryId = ""
           areaName = ""
           areaId = ""
           industryId = ""
           industryName = ""
           salary = 0
           onlyWithSalary = false
       }
   }


    fun isFilterParametersNotEmpty(filterParameters: FilterParameters) : Boolean {
        with(filterParameters){
            return if(countryId != "" || areaId != "" || industryId != "" || salary != 0 || onlyWithSalary){
                true
            } else{
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
