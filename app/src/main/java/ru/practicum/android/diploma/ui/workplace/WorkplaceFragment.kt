package ru.practicum.android.diploma.ui.workplace

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
    private var isGetCountry = false

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

        viewModel.country.observe(viewLifecycleOwner) { country ->
            countryBundle = country
            binding.countryEditText.setText(country.countryName)
        }

        viewModel.region.observe(viewLifecycleOwner) { region ->
            regionBundle = region
            binding.regionEditText.setText(region.regionName)
            if (isGetCountry) {
                Log.d("logk", "region.parentId: ${region.parentId}")
                viewModel.getCountryById(region.parentId.toString())
            }
        }

        binding.countryEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val iconRes = if (s.isNullOrEmpty()) R.drawable.ic_forward else R.drawable.ic_close
                binding.inputCountry.endIconDrawable = ContextCompat.getDrawable(requireContext(), iconRes)
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
                isGetCountry = false
                binding.countryEditText.text?.clear()
                binding.regionEditText.text?.clear()
                viewModel.setCountry(Country("", ""))
                viewModel.setRegion(Region("", "", ""))
            } else {
                goToCountryFragment()
            }
        }

        binding.regionEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val iconRes = if (s.isNullOrEmpty()) R.drawable.ic_forward else R.drawable.ic_close
                binding.inputRegion.endIconDrawable = ContextCompat.getDrawable(requireContext(), iconRes)
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
                viewModel.setRegion(Region("", "", ""))
            } else {
                goToRegionFragment()
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
                viewModel.setRegion(Region("", "", ""))
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
            goToCountryFragment()
        }

        binding.regionEditText.setOnClickListener {
            goToRegionFragment()
        }

        binding.selectButton.setOnClickListener {
            setFragmentResult(
                "filter_key",
                androidx.core.os.bundleOf(
                    "country" to countryBundle,
                    "region" to regionBundle
                )
            )
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun goToCountryFragment() {
        isGetCountry = false
        findNavController().navigate(WorkplaceFragmentDirections.actionWorkplaceFragmentToCountryFragment())
    }

    private fun goToRegionFragment() {
        isGetCountry = true
        val country = countryBundle?: Country("", "")
        val countryJson = Gson().toJson(country)
        val bundle = bundleOf(COUNTRY to countryJson)

        findNavController().navigate(
            R.id.action_workplaceFragment_to_regionFragment,
            bundle
        )
    }

    companion object {
        private const val SENDING_DATA_KEY = "sendingDataKey"
        private const val COUNTRY = "country"
        private const val REGION = "region"
    }
}
