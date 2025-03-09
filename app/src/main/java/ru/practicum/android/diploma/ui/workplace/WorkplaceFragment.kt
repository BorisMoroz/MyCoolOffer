package ru.practicum.android.diploma.ui.workplace

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.bundle.bundleOf
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentWorkplaceBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region

class WorkplaceFragment : Fragment() {
    private var _binding: FragmentWorkplaceBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<WorkplaceViewModel>()

    private var countryBundle: Country? = null
    private var regionBundle: Region? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkSelectButtonVisibility()

        viewModel.country.observe(viewLifecycleOwner) { country ->
            countryBundle = country
            binding.countryEditText.setText(country.countryName)
        }

        viewModel.region.observe(viewLifecycleOwner) { region ->
            binding.regionEditText.setText(region.regionName)
            viewModel.getCountryById(region.parentId.toString())
        }

        binding.countryEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val iconRes = if (s.isNullOrEmpty()) R.drawable.ic_forward else R.drawable.ic_close
                binding.inputCountry.endIconDrawable = ContextCompat.getDrawable(requireContext(), iconRes)
                checkSelectButtonVisibility()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Метод не используется, но нужен для интерфейса TextWatcher
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Метод не используется, но нужен для интерфейса TextWatcher
            }
        })

        binding.inputCountry.setEndIconOnClickListener {
            if (!binding.countryEditText.text.isNullOrEmpty()) {
                binding.countryEditText.text?.clear()
                viewModel.setCountry(Country(EMPTY_STRING, EMPTY_STRING))
                checkSelectButtonVisibility()
            }
        }

        binding.regionEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val iconRes = if (s.isNullOrEmpty()) R.drawable.ic_forward else R.drawable.ic_close
                binding.inputRegion.endIconDrawable = ContextCompat.getDrawable(requireContext(), iconRes)
                checkSelectButtonVisibility()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Метод не используется, но нужен для интерфейса TextWatcher
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Метод не используется, но нужен для интерфейса TextWatcher
            }
        })

        binding.inputRegion.setEndIconOnClickListener {
            if (!binding.regionEditText.text.isNullOrEmpty()) {
                binding.regionEditText.text?.clear()
                viewModel.setRegion(Region(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING))
                checkSelectButtonVisibility()
            }
        }

        parentFragmentManager.setFragmentResultListener(
            SENDING_DATA_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(COUNTRY)?.takeIf { it.isNotEmpty() }?.let { countryJson ->
                val country: Country = Gson().fromJson(countryJson, Country::class.java)
                countryBundle = country
                viewModel.setCountry(country)
            }

            bundle.getString(REGION)?.takeIf { it.isNotEmpty() }?.let { regionJson ->
                val region: Region = Gson().fromJson(regionJson, Region::class.java)
                regionBundle = region
                viewModel.setRegion(region)
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.countryEditText.setOnClickListener {
            findNavController().navigate(WorkplaceFragmentDirections.actionWorkplaceFragmentToCountryFragment())
        }

        binding.regionEditText.setOnClickListener {
            var country = Country(EMPTY_STRING, EMPTY_STRING)
            viewModel.country.observe(viewLifecycleOwner) { _country ->
                country = _country
            }
            val countryJson = Gson().toJson(country)
            val bundle = bundleOf(COUNTRY to countryJson)

            findNavController().navigate(
                R.id.action_workplaceFragment_to_regionFragment,
                bundle
            )
        }

        binding.selectButton.setOnClickListener {
            setFragmentResult(
                FILTER_KEY,
                androidx.core.os.bundleOf(
                    COUNTRY to countryBundle,
                    REGION to regionBundle
                )
            )
            findNavController().navigateUp()
        }
    }

    private fun checkSelectButtonVisibility() {
        if (binding.countryEditText.text.isNullOrEmpty() && binding.regionEditText.text.isNullOrEmpty()) {
            binding.selectButton.visibility = View.GONE
        } else {
            binding.selectButton.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val SENDING_DATA_KEY = "sendingDataKey"
        const val COUNTRY = "country"
        const val REGION = "region"
        const val FILTER_KEY = "filter_key"
        const val EMPTY_STRING = ""
    }
}
