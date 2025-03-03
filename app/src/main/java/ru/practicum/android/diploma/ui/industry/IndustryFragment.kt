package ru.practicum.android.diploma.ui.industry

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Industry

class IndustryFragment : Fragment() {
    private var _binding: FragmentIndustryBinding? = null
    private val binding get() = _binding!!

    private lateinit var industryAdapter: IndustryAdapter
    private lateinit var inputMethod: InputMethodManager

    private val viewModel by viewModel<IndustryViewModel>()

    var industries = mutableListOf<Industry>()
    var selectedIndustry: Industry? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputMethod = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        industryAdapter = IndustryAdapter(industries, onChoosedIndustry)

        binding.industriesList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.industriesList.adapter = industryAdapter

        viewModel.searchIndustries("")

        viewModel.getGetIndustriesState().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        val inputTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // здесь можно не реализовывать
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.clearOrSearchButton.setImageResource(R.drawable.ic_search)
                } else {
                    binding.clearOrSearchButton.setImageResource(R.drawable.ic_close)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.searchIndustries(s.toString())
            }
        }

        binding.industryEdittext.addTextChangedListener(inputTextWatcher)

        binding.industryEdittext.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                inputMethod.hideSoftInputFromWindow(binding.industryEdittext.windowToken, 0)
            }
            false
        }

        binding.clearOrSearchButton.setOnClickListener { binding.industryEdittext.setText("") }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    val onChoosedIndustry: (selectedIndustry: Industry?) -> Unit = { selected ->
        binding.buttonApply.isVisible = true

        selectedIndustry = selected
    }

    fun renderState(state: GetIndustriesState?) {
        when (state) {
            is GetIndustriesState.Error -> showError(state.errorCode)
            is GetIndustriesState.Content -> showIndustries(state.data)
            else -> {
            }
        }
    }

    private fun showError(errorCode: Int) {
        industries.clear()
        industryAdapter.notifyDataSetChanged()

        binding.buttonApply.isVisible = false
        binding.getIndustriesErrorLayout.isVisible = true
    }

    private fun showIndustries(data: Industries) {
        industries.clear()
        industries.addAll(data.items)

        if (industries.isEmpty()) {
            binding.noIndustryFoundLayout.isVisible = true
            binding.buttonApply.isVisible = false
        } else {
            binding.noIndustryFoundLayout.isVisible = false
            if (selectedIndustry == null) {
                binding.buttonApply.isVisible = false
            } else {
                binding.buttonApply.isVisible = true
            }
        }

        industryAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
