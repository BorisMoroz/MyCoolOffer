package ru.practicum.android.diploma.ui.region

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
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.ui.country.AreaConverter
import ru.practicum.android.diploma.util.NETWORK_CONNECTION_ERROR

class RegionFragment : Fragment(), OnRegionClickListener {
    private var _binding: FragmentRegionBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<RegionViewModel>()
    private var country = Country("", "")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val countryJson = arguments?.getString(COUNTRY)
        country = Gson().fromJson(countryJson, Country::class.java)

        if (country.countryId.isNullOrEmpty()) {
            viewModel.getRegions("113")
        } else {
            viewModel.getRegions(country.countryId)
        }

        viewModel.getRegionState().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun render(state: RegionState) {
        when (state) {
            is RegionState.Loading -> {
                showLoading()
            }

            is RegionState.Error -> {
                if (state.errorCode == NETWORK_CONNECTION_ERROR) {
                    showInternetConnectionError()
                } else {

                }
            }

            is RegionState.Content -> {
                showResult(state.areas)
            }
        }
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.listRegions.visibility = View.GONE
        binding.containerPlaceholder.visibility = View.GONE
    }

    private fun showInternetConnectionError() {
        binding.progress.visibility = View.GONE
        binding.listRegions.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_connection_error)
        binding.textPlaceholder.text = getString(R.string.connection_error)
        binding.containerPlaceholder.visibility = View.VISIBLE
    }

    private fun showError() {
        binding.progress.visibility = View.GONE
        binding.listRegions.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_search_error)
        binding.textPlaceholder.text = getString(R.string.failed_to_get_regions)
        binding.containerPlaceholder.visibility = View.VISIBLE
    }

    private fun showResult(areas: List<Area>) {
        binding.progress.visibility = View.GONE
        binding.listRegions.adapter = RegionAdapter(mapAreaListToRegionList(areas), this)
        binding.listRegions.visibility = View.VISIBLE
        binding.containerPlaceholder.visibility = View.GONE
    }

    private fun mapAreaListToRegionList(areas: List<Area>): List<Region> {
        val convertor = AreaConverter()
        var areaList = mutableListOf<Region>()
        for (item in areas) {
            areaList.add(convertor.mapToRegion(item))
        }
        return areaList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRegionClick(region: Region) {
        val regionJson = Gson().toJson(region)
        parentFragmentManager.setFragmentResult(
            SENDING_DATA_KEY,
            bundleOf(REGION to regionJson)
        )
        findNavController().navigateUp()
    }

    companion object {
        private const val SENDING_DATA_KEY = "sendingDataKey"
        private const val REGION = "region"
        private const val COUNTRY = "country"
    }
}
