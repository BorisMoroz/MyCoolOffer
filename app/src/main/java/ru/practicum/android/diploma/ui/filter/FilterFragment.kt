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

    private lateinit var currentFilterParameters: FilterParameters

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
            delay(50)
            currentFilterParameters = getCurrentFilterParameters()
            setupWorkplaceLayout()
            setupIndustryLayout()
            setupSalaryEditText()
            setupSalaryCheckBox()
            setupWorkplaceAndIndustryResetButtons()
            setupButtons()
        }
    }

    fun hideKeyBoard() {
        val inputMethod = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethod.hideSoftInputFromWindow(binding.salaryEdittext.windowToken, 0)
    }

    fun setupWorkplaceLayout() {
        if (currentFilterParameters.areaId.isEmpty()) {
            binding.workplaceLayout1.visibility = View.VISIBLE
            binding.workplaceLayout2.visibility = View.INVISIBLE
        } else {
            binding.workplaceLayout1.visibility = View.INVISIBLE
            binding.workplaceLayout2.visibility = View.VISIBLE

            binding.workplaceName.text = currentFilterParameters.countryName

            if (currentFilterParameters.countryId != currentFilterParameters.areaId) {
                binding.workplaceName.text =
                    currentFilterParameters.countryName + ", " + currentFilterParameters.areaName
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

    fun setupIndustryLayout() {
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

    fun setupSalaryEditText() {
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
            binding.salaryEdittext.setText("")
        }
    }

    fun setupSalaryCheckBox() {
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

    fun setupWorkplaceAndIndustryResetButtons() {
        binding.workplaceButtonReset.setOnClickListener {
            binding.workplaceLayout1.visibility = View.VISIBLE
            binding.workplaceLayout2.visibility = View.INVISIBLE

            with(currentFilterParameters) {
                countryName = ""
                countryId = ""
                areaName = ""
                areaId = ""
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
                industryName = ""
                industryId = ""
            }

            saveCurrentFilterParameters()
            updateButtons()

            hideKeyBoard()
            binding.salaryEdittext.clearFocus()
        }
    }

    fun setupButtons() {
        if (isFilterParametersNotEmpty(currentFilterParameters)) {
            binding.buttonReset.visibility = View.VISIBLE
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
        }

        binding.buttonApply.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    fun updatesSalary() {
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

    fun updateButtons() {
        if (isFilterParametersNotEmpty(currentFilterParameters)) {
            binding.buttonReset.visibility = View.VISIBLE
        } else {
            binding.buttonReset.visibility = View.INVISIBLE
        }
        binding.buttonApply.visibility = View.VISIBLE
    }

    fun getCurrentFilterParameters(): FilterParameters {
        val filterSettings = viewModel.getFilterSettings()

        return FilterParameters(
            countryId = filterSettings["countryId"] ?: "",
            countryName = filterSettings["countryName"] ?: "",
            areaId = filterSettings["areaId"] ?: "",
            areaName = filterSettings["areaName"] ?: "",
            industryId = filterSettings["industryId"] ?: "",
            industryName = filterSettings["industryName"] ?: "",
            salary = filterSettings["salary"]?.toIntOrNull() ?: 0,
            onlyWithSalary = filterSettings["onlyWithSalary"]?.toBoolean() ?: false
        )
    }

    fun saveCurrentFilterParameters() {
        viewModel.saveFilterSettings(
            mapOf(
                "countryId" to currentFilterParameters.countryId,
                "countryName" to currentFilterParameters.countryName,
                "areaId" to currentFilterParameters.areaId,
                "areaName" to currentFilterParameters.areaName,
                "industryId" to currentFilterParameters.industryId,
                "industryName" to currentFilterParameters.industryName,
                "salary" to currentFilterParameters.salary.toString(),
                "onlyWithSalary" to currentFilterParameters.onlyWithSalary.toString(),
            )
        )
    }

    fun clearCurrentFilterParameters() {
        with(currentFilterParameters) {
            countryName = ""
            countryId = ""
            areaName = ""
            areaId = ""
            industryId = ""
            industryName = ""
            salary = 0
            onlyWithSalary = false
        }
        viewModel.clearFilterSettings()
    }

    fun isFilterParametersNotEmpty(filterParameters: FilterParameters): Boolean {
        with(filterParameters) {
            val checkArea = countryId != "" || areaId != ""
            val checkIndustryAndSalary = industryId != "" || salary != 0 || onlyWithSalary

            return if (checkArea || checkIndustryAndSalary) {
                true
            } else {
                false
            }
        }
    }

    private fun getResultFilter() {
        setFragmentResultListener("filter_key") { _, bundle ->
            bundle.keySet().forEach { key ->
                val value = bundle.getString(key)
                if (value != null) {
                    when (key) {
                        "industryId" -> {
                            currentFilterParameters.industryId = value
                            viewModel.saveFilterSettings(mapOf("industryId" to value))
                        }

                        "industryName" -> {
                            binding.industryName.text = currentFilterParameters.industryName
                            currentFilterParameters.industryName = value
                            viewModel.saveFilterSettings(mapOf("industryName" to value))
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
}
