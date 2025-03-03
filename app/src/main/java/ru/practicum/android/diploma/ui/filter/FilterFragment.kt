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
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.domain.models.FilterParameters

class FilterFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FilterViewModel>()

    private val currentFilterParameters: FilterParameters by lazy {
        currentFilterParameters()
    }

    var salaryChanged = false

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

        getResultFilter()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        lifecycle.coroutineScope.launch {
            delay(DELAY)
            setupWorkplaceLayout()
            setupIndustryLayout()
            setupSalaryEditText()
            setupSalaryCheckBox()
            setupWorkplaceAndIndustryResetButtons()
            setupButtons()
        }
    }

    private fun hideKeyBoard() {
        val inputMethod = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethod.hideSoftInputFromWindow(binding.salaryEdittext.windowToken, 0)
    }

    private fun setupWorkplaceLayout() {
        if (currentFilterParameters.areaId.isEmpty()) {
            binding.workplaceLayout1.visibility = View.VISIBLE
            binding.workplaceLayout2.visibility = View.INVISIBLE
        } else {
            binding.workplaceLayout1.visibility = View.INVISIBLE
            binding.workplaceLayout2.visibility = View.VISIBLE

            binding.workplaceName.text = currentFilterParameters.countryName

            if (currentFilterParameters.countryId != currentFilterParameters.areaId) {
                val text = currentFilterParameters.countryName + ", " + currentFilterParameters.areaName
                binding.workplaceName.text = text
            }
        }
        binding.workplaceLayout1.setOnClickListener {
            hideKeyBoard()
            binding.salaryEdittext.clearFocus()
            findNavController().navigate(R.id.action_filterFragment_to_workplaceFragment)
        }
        binding.workplaceTitle.setOnClickListener {
            hideKeyBoard()
            binding.salaryEdittext.clearFocus()
            findNavController().navigate(R.id.action_filterFragment_to_workplaceFragment)
        }
        binding.workplaceName.setOnClickListener {
            hideKeyBoard()
            binding.salaryEdittext.clearFocus()
            findNavController().navigate(R.id.action_filterFragment_to_workplaceFragment)
        }
    }

    private fun setupIndustryLayout() {
        if (currentFilterParameters.industryId.isEmpty()) {
            binding.industryLayout1.visibility = View.VISIBLE
            binding.industryLayout2.visibility = View.INVISIBLE
        } else {
            binding.industryLayout1.visibility = View.INVISIBLE
            binding.industryLayout2.visibility = View.VISIBLE

            binding.industryName.text = currentFilterParameters.industryName
        }

        if (currentFilterParameters.salary != 0) {
            binding.salaryEdittext.setText(currentFilterParameters.salary.toString())
            val color = ContextCompat.getColor(requireContext(), R.color.yp_black)
            binding.salaryTitle.setTextColor(color)
        }

        binding.industryLayout1.setOnClickListener {
            hideKeyBoard()
            binding.salaryEdittext.clearFocus()
            findNavController().navigate(R.id.action_filterFragment_to_industryFragment)
        }
        binding.industryTitle.setOnClickListener {
            hideKeyBoard()
            binding.salaryEdittext.clearFocus()
            findNavController().navigate(R.id.action_filterFragment_to_industryFragment)
        }
        binding.industryName.setOnClickListener {
            hideKeyBoard()
            binding.salaryEdittext.clearFocus()
            findNavController().navigate(R.id.action_filterFragment_to_industryFragment)
        }
    }

    private fun setupSalaryEditText() {
        val inputTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // здесь можно не реализовывать
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                salaryChanged = true
                binding.buttonApply.isVisible = true
            }
        }
        binding.salaryEdittext.addTextChangedListener(inputTextWatcher)

        binding.salaryEdittext.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && (view as EditText).text.isNotEmpty()) {
                binding.clearButton.isVisible = true
            } else {
                binding.clearButton.isVisible = false
            }
            if (hasFocus) {
                val color = ContextCompat.getColor(requireContext(), R.color.yp_blue)
                binding.salaryTitle.setTextColor(color)
            } else {
                if (binding.salaryEdittext.text.isNotEmpty()) {
                    val color = ContextCompat.getColor(requireContext(), R.color.yp_black)
                    binding.salaryTitle.setTextColor(color)
                } else {
                    val color = ContextCompat.getColor(requireContext(), R.color.yp_gray)
                    binding.salaryTitle.setTextColor(color)
                }
                updatesSalary()
            }
        }
        binding.salaryLayout.setOnClickListener {
            binding.salaryEdittext.requestFocus()
        }
        binding.salaryEdittext.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                hideKeyBoard()
                updatesSalary()
            }
            false
        }
        binding.clearButton.setOnClickListener {
            binding.salaryEdittext.setText(EMPTY_STRING)
        }
    }

    private fun setupSalaryCheckBox() {
        binding.salaryCheckBox.isChecked = currentFilterParameters.onlyWithSalary

        binding.salaryCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            with(currentFilterParameters) {
                onlyWithSalary = isChecked
            }

            saveCurrentFilterParameters()
            updateButtons()

            hideKeyBoard()
            binding.salaryEdittext.clearFocus()
        }
    }

    private fun setupWorkplaceAndIndustryResetButtons() {
        binding.workplaceButtonReset.setOnClickListener {
            binding.workplaceLayout1.visibility = View.VISIBLE
            binding.workplaceLayout2.visibility = View.INVISIBLE

            with(currentFilterParameters) {
                countryName = EMPTY_STRING
                countryId = EMPTY_STRING
                areaName = EMPTY_STRING
                areaId = EMPTY_STRING
            }

            saveCurrentFilterParameters()
            updateButtons()

            hideKeyBoard()
            binding.salaryEdittext.clearFocus()
        }

        binding.industryButtonReset.setOnClickListener {
            binding.industryLayout1.visibility = View.VISIBLE
            binding.industryLayout2.visibility = View.INVISIBLE

            with(currentFilterParameters) {
                industryName = EMPTY_STRING
                industryId = EMPTY_STRING
            }

            saveCurrentFilterParameters()
            updateButtons()

            hideKeyBoard()
            binding.salaryEdittext.clearFocus()
        }
    }

    private fun setupButtons() {
        if (isFilterParametersNotEmpty(currentFilterParameters)) {
            binding.buttonReset.visibility = View.VISIBLE
        }

        binding.buttonReset.setOnClickListener {
            binding.workplaceLayout1.visibility = View.VISIBLE
            binding.workplaceLayout2.visibility = View.INVISIBLE
            binding.industryLayout1.visibility = View.VISIBLE
            binding.industryLayout2.visibility = View.INVISIBLE

            binding.salaryEdittext.setText(EMPTY_STRING)
            binding.salaryEdittext.clearFocus()

            binding.salaryCheckBox.isChecked = false

            binding.buttonReset.visibility = View.INVISIBLE

            clearCurrentFilterParameters()
        }

        binding.buttonApply.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun updatesSalary() {
        if (salaryChanged) {
            with(currentFilterParameters) {
                if (binding.salaryEdittext.text.isNotEmpty()) {
                    salary = binding.salaryEdittext.text.toString().toInt()
                } else {
                    salary = 0
                }
            }
            saveCurrentFilterParameters()
            updateButtons()
            salaryChanged = false
        }
    }

    private fun updateButtons() {
        if (isFilterParametersNotEmpty(currentFilterParameters)) {
            binding.buttonReset.visibility = View.VISIBLE
        } else {
            binding.buttonReset.visibility = View.INVISIBLE
        }
        binding.buttonApply.visibility = View.VISIBLE
    }

    private fun currentFilterParameters(): FilterParameters {
        val filterSettings = viewModel.getFilterSettings()

        return FilterParameters(
            countryId = filterSettings[COUNTRY_ID].orEmpty(),
            countryName = filterSettings[COUNTRY_NAME].orEmpty(),
            areaId = filterSettings[AREA_ID].orEmpty(),
            areaName = filterSettings[AREA_NAME].orEmpty(),
            industryId = filterSettings[INDUSTRY_ID].orEmpty(),
            industryName = filterSettings[INDUSTRY_NAME].orEmpty(),
            salary = filterSettings[SALARY]?.toIntOrNull() ?: 0,
            onlyWithSalary = filterSettings[ONLY_WITH_SALARY]?.toBoolean() ?: false
        )
    }

    private fun saveCurrentFilterParameters() {
        viewModel.saveFilterSettings(
            mapOf(
                COUNTRY_ID to currentFilterParameters.countryId,
                COUNTRY_NAME to currentFilterParameters.countryName,
                AREA_ID to currentFilterParameters.areaId,
                AREA_NAME to currentFilterParameters.areaName,
                INDUSTRY_ID to currentFilterParameters.industryId,
                INDUSTRY_NAME to currentFilterParameters.industryName,
                SALARY to currentFilterParameters.salary.toString(),
                ONLY_WITH_SALARY to currentFilterParameters.onlyWithSalary.toString(),
            )
        )
    }

    private fun clearCurrentFilterParameters() {
        with(currentFilterParameters) {
            countryName = EMPTY_STRING
            countryId = EMPTY_STRING
            areaName = EMPTY_STRING
            areaId = EMPTY_STRING
            industryId = EMPTY_STRING
            industryName = EMPTY_STRING
            salary = 0
            onlyWithSalary = false
        }
        viewModel.clearFilterSettings()
    }

    private fun isFilterParametersNotEmpty(filterParameters: FilterParameters): Boolean {
        with(filterParameters) {
            val checkArea = countryId != EMPTY_STRING || areaId != EMPTY_STRING
            val checkIndustryAndSalary = industryId != EMPTY_STRING || salary != 0 || onlyWithSalary

            return if (checkArea || checkIndustryAndSalary) {
                true
            } else {
                false
            }
        }
    }

    private fun getResultFilter() {
        setFragmentResultListener(FILTER_KEY) { _, bundle ->
            bundle.keySet().forEach { key ->
                val value = bundle.getString(key)
                if (value != null) {
                    when (key) {
                        INDUSTRY_ID -> {
                            currentFilterParameters.industryId = value
                            viewModel.saveFilterSettings(mapOf(INDUSTRY_ID to value))
                        }

                        INDUSTRY_NAME -> {
                            binding.industryName.text = currentFilterParameters.industryName
                            currentFilterParameters.industryName = value
                            viewModel.saveFilterSettings(mapOf(INDUSTRY_NAME to value))
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val DELAY: Long = 50
        const val COUNTRY_ID = "countryId"
        const val COUNTRY_NAME = "countryName"
        const val AREA_ID = "areaId"
        const val AREA_NAME = "areaName"
        const val INDUSTRY_ID = "industryId"
        const val INDUSTRY_NAME = "industryName"
        const val SALARY = "salary"
        const val ONLY_WITH_SALARY = "onlyWithSalary"
        const val EMPTY_STRING = ""
        const val FILTER_KEY = "filter_key"
    }
}
