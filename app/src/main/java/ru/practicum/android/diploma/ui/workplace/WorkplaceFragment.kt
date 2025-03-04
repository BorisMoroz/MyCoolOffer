package ru.practicum.android.diploma.ui.workplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.bundle.bundleOf
import androidx.fragment.app.Fragment
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
            binding.countryEditText.setText(country.countryName)
        }

        viewModel.region.observe(viewLifecycleOwner) { region ->
            binding.regionEditText.setText(region.regionName)
            viewModel.getCountryById(region.parentId.toString())
        }

        parentFragmentManager.setFragmentResultListener(
            SENDING_DATA_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(COUNTRY)?.takeIf { it.isNotEmpty() }?.let { countryJson ->
                viewModel.setCountry(Gson().fromJson(countryJson, Country::class.java))
            }

            bundle.getString(REGION)?.takeIf { it.isNotEmpty() }?.let { regionJson ->
                viewModel.setRegion(Gson().fromJson(regionJson, Region::class.java))
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.countryEditText.setOnClickListener {
            findNavController().navigate(WorkplaceFragmentDirections.actionWorkplaceFragmentToCountryFragment())
        }

        binding.regionEditText.setOnClickListener {
            var country = Country("", "")
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
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SENDING_DATA_KEY = "sendingDataKey"
        private const val COUNTRY = "country"
        private const val REGION = "region"
    }
}
