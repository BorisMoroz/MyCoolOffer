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
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.Region

class FilterFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FilterViewModel>()

    private var _originalFilterParameters: FilterParameters? = null
    private val originalFilterParameters get() = _originalFilterParameters!!

    private var _currentFilterParameters: FilterParameters? = null
    private val currentFilterParameters get() = _currentFilterParameters!!

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

        currentFilterParameters()
        getResultFilter()

        _originalFilterParameters = viewModel.restoreOriginalFilterParameters()

        if (_originalFilterParameters == null) {
            _originalFilterParameters = currentFilterParameters.copy()
            viewModel.saveOriginalFilterParameters(originalFilterParameters)
        }

        binding.toolbar.setNavigationOnClickListener {
            viewModel.clearOriginalFilterParameters()

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
            updateButtons()
        }
    }

    private fun hideKeyBoard() {
        val inputMethod = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethod.hideSoftInputFromWindow(binding.salaryEdittext.windowToken, 0)
    }

    private fun setupWorkplaceLayout() {
        if (currentFilterParameters.areaId.isEmpty() && currentFilterParameters.countryId.isEmpty()) {
            binding.workplaceLayout1.visibility = View.VISIBLE
            binding.workplaceLayout2.visibility = View.INVISIBLE
        } else {
            binding.workplaceLayout1.visibility = View.INVISIBLE
            binding.workplaceLayout2.visibility = View.VISIBLE

            binding.workplaceName.text = currentFilterParameters.countryName

            if (currentFilterParameters.countryId != currentFilterParameters.areaId
                && currentFilterParameters.areaId != EMPTY_STRING) {
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

            navigateToWorkPlaceFragment()
        }
        binding.workplaceName.setOnClickListener {
            hideKeyBoard()
            binding.salaryEdittext.clearFocus()

            navigateToWorkPlaceFragment()
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

        if (currentFilterParameters.salary != EMPTY_STRING) {
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

            navigateToIndustriesFragment()
        }
        binding.industryName.setOnClickListener {
            hideKeyBoard()
            binding.salaryEdittext.clearFocus()

            navigateToIndustriesFragment()
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
                // здесь можно не реализовывать
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
                updateSalary()
            }
        }
        binding.salaryLayout.setOnClickListener {
            binding.salaryEdittext.requestFocus()
        }
        binding.salaryEdittext.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                hideKeyBoard()
                updateSalary()
            }
            false
        }
        binding.clearButton.setOnClickListener {
            binding.salaryEdittext.setText(EMPTY_STRING)

            updateSalary()
        }
    }

    private fun setupSalaryCheckBox() {
        binding.salaryCheckBox.isChecked = currentFilterParameters.onlyWithSalary

        binding.salaryCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            _currentFilterParameters = currentFilterParameters.copy(onlyWithSalary = isChecked)

            saveCurrentFilterParameters()
            updateButtons()

            hideKeyBoard()
            binding.salaryEdittext.clearFocus()
        }
    }

    private fun setupWorkplaceAndIndustryResetButtons() {
        binding.workplaceButtonReset.setOnClickListener {
            binding.workplaceLayout2.visibility = View.INVISIBLE
            binding.workplaceLayout1.visibility = View.VISIBLE

            _currentFilterParameters = currentFilterParameters.copy(
                countryName = EMPTY_STRING,
                countryId = EMPTY_STRING,
                areaName = EMPTY_STRING,
                areaId = EMPTY_STRING
            )

            saveCurrentFilterParameters()
            updateButtons()

            hideKeyBoard()
            binding.salaryEdittext.clearFocus()
        }

        binding.industryButtonReset.setOnClickListener {
            binding.industryLayout2.visibility = View.INVISIBLE
            binding.industryLayout1.visibility = View.VISIBLE

            _currentFilterParameters = currentFilterParameters.copy(
                industryName = EMPTY_STRING,
                industryId = EMPTY_STRING
            )

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
            updateButtons()
        }

        binding.buttonApply.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun updateSalary() {
        _currentFilterParameters = if (binding.salaryEdittext.text.isNotEmpty()) {
            currentFilterParameters.copy(
                salary = binding.salaryEdittext.text.toString()
            )
        } else {
            currentFilterParameters.copy(
                salary = EMPTY_STRING
            )
        }
        saveCurrentFilterParameters()
        updateButtons()
    }

    private fun updateButtons() {
        if (isFilterParametersNotEmpty(currentFilterParameters)) {
            binding.buttonReset.visibility = View.VISIBLE
        } else {
            binding.buttonReset.visibility = View.INVISIBLE
        }

        if (currentFilterParameters != originalFilterParameters) {
            binding.buttonApply.visibility = View.VISIBLE
        } else {
            binding.buttonApply.visibility = View.INVISIBLE
        }
    }

    private fun currentFilterParameters() {
        val filterSettings = viewModel.getFilterSettings()

        _currentFilterParameters = FilterParameters(
            countryId = filterSettings[COUNTRY_ID].orEmpty(),
            countryName = filterSettings[COUNTRY_NAME].orEmpty(),
            areaId = filterSettings[AREA_ID].orEmpty(),
            areaName = filterSettings[AREA_NAME].orEmpty(),
            industryId = filterSettings[INDUSTRY_ID].orEmpty(),
            industryName = filterSettings[INDUSTRY_NAME].orEmpty(),
            salary = filterSettings[SALARY].orEmpty(),
            onlyWithSalary = filterSettings[ONLY_WITH_SALARY]?.toBoolean() ?: false
        )
    }

    private fun saveCurrentFilterParameters() {
        if (isFilterParametersNotEmpty(currentFilterParameters)) {
            viewModel.saveFilterSettings(
                mapOf(
                    COUNTRY_ID to currentFilterParameters.countryId,
                    COUNTRY_NAME to currentFilterParameters.countryName,
                    AREA_ID to currentFilterParameters.areaId,
                    AREA_NAME to currentFilterParameters.areaName,
                    INDUSTRY_ID to currentFilterParameters.industryId,
                    INDUSTRY_NAME to currentFilterParameters.industryName,
                    SALARY to currentFilterParameters.salary,
                    ONLY_WITH_SALARY to currentFilterParameters.onlyWithSalary.toString(),
                )
            )
        } else {
            viewModel.clearFilterSettings()
        }
    }

    private fun clearCurrentFilterParameters() {
        _currentFilterParameters = currentFilterParameters.copy(
            countryName = EMPTY_STRING,
            countryId = EMPTY_STRING,
            areaName = EMPTY_STRING,
            areaId = EMPTY_STRING,
            industryId = EMPTY_STRING,
            industryName = EMPTY_STRING,
            salary = EMPTY_STRING,
            onlyWithSalary = false
        )
        viewModel.clearFilterSettings()
    }

    private fun isFilterParametersNotEmpty(filterParameters: FilterParameters): Boolean {
        with(filterParameters) {
            val checkArea = countryId != EMPTY_STRING || areaId != EMPTY_STRING
            val checkIndustryAndSalary = industryId != EMPTY_STRING || salary != EMPTY_STRING || onlyWithSalary

            return if (checkArea || checkIndustryAndSalary) {
                true
            } else {
                false
            }
        }
    }

    private fun navigateToIndustriesFragment() {
        setFragmentResult(
            "industry_key",
            bundleOf(
                "industryName" to currentFilterParameters.industryName,
                "industryId" to currentFilterParameters.industryId
            )
        )
        findNavController().navigate(R.id.action_filterFragment_to_industryFragment)
    }

    private fun navigateToWorkPlaceFragment() {
        val countryJson = Gson().toJson(Country(currentFilterParameters.countryId, currentFilterParameters.countryName))
        val regionJson = Gson().toJson(
            Region(
                currentFilterParameters.areaId,
                currentFilterParameters.countryId,
                currentFilterParameters.areaName
            )
        )
        setFragmentResult(
            "sendingDataKey",
            bundleOf(
                "country" to countryJson,
                "region" to regionJson
            )
        )
        findNavController().navigate(R.id.action_filterFragment_to_workplaceFragment)
    }

    private fun getResultFilter() {
        setFragmentResultListener(FILTER_KEY) { _, bundle ->
            bundle.keySet().forEach { key ->
                when (key) {
                    INDUSTRY_ID -> {
                        val industryId = bundle.getString(INDUSTRY_ID)
                        setIndustryId(industryId)
                    }

                    INDUSTRY_NAME -> {
                        val industryName = bundle.getString(INDUSTRY_NAME)
                        setIndustryName(industryName)
                    }

                    COUNTRY -> {
                        val country = bundle.getParcelable<Country>(COUNTRY)
                        setCountry(country)
                    }

                    REGION -> {
                        val region = bundle.getParcelable<Region>(REGION)
                        setRegion(region)
                    }

                    else -> {}
                }
            }
            updateButtons()
        }
    }

    private fun setIndustryId(industryId: String?) {
        if (industryId != null) {
            _currentFilterParameters = currentFilterParameters.copy(
                industryId = industryId
            )
            viewModel.saveFilterSettings(mapOf(INDUSTRY_ID to industryId))
        }
    }

    private fun setIndustryName(industryName: String?) {
        if (industryName != null) {
            _currentFilterParameters = currentFilterParameters.copy(
                industryName = industryName
            )
            viewModel.saveFilterSettings(mapOf(INDUSTRY_NAME to industryName))
            binding.industryName.text = currentFilterParameters.industryName
        }
    }

    private fun setCountry(country: Country?) {
        if (country != null) {
            _currentFilterParameters = currentFilterParameters.copy(
                countryId = country.countryId,
                countryName = country.countryName
            )
            viewModel.saveFilterSettings(
                mapOf(
                    COUNTRY_ID to country.countryId,
                    COUNTRY_NAME to country.countryName
                )
            )
            binding.workplaceName.text = _currentFilterParameters?.countryName
        }
    }

    private fun setRegion(region: Region?) {
        if (region != null) {
            _currentFilterParameters = currentFilterParameters.copy(
                areaId = region.regionId,
                areaName = region.regionName
            )
            viewModel.saveFilterSettings(
                mapOf(
                    AREA_ID to region.regionId,
                    AREA_NAME to region.regionName
                )
            )
            binding.workplaceName.text =
                "${_currentFilterParameters?.countryName}, ${_currentFilterParameters?.areaName}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _currentFilterParameters = null
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
        const val COUNTRY = "country"
        const val REGION = "region"
    }
}
