package ru.practicum.android.diploma.ui.workplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentWorkplaceBinding

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

        viewModel.countryId.observe(viewLifecycleOwner) { countryId ->
            binding.countryEditText.setText(countryId)
        }

        viewModel.regionId.observe(viewLifecycleOwner) { regionId ->
            binding.regionEditText.setText(regionId)
        }

        parentFragmentManager.setFragmentResultListener(
            SENDING_DATA_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(COUNTRY_ID_KEY)?.let { viewModel.setCountryId(it) }
            bundle.getString(REGION_ID_KEY)?.let { viewModel.setRegionId(it) }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.countryEditText.setOnClickListener {
            findNavController().navigate(WorkplaceFragmentDirections.actionWorkplaceFragmentToCountryFragment())
        }

        binding.regionEditText.setOnClickListener {
            findNavController().navigate(WorkplaceFragmentDirections.actionWorkplaceFragmentToRegionFragment())
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
        private const val COUNTRY_ID_KEY = "countryIdKey"
        private const val REGION_ID_KEY = "regionIdKey"
    }
}
