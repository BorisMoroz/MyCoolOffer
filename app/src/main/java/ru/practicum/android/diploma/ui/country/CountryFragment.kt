package ru.practicum.android.diploma.ui.country

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
import ru.practicum.android.diploma.databinding.FragmentCountryBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.util.NETWORK_CONNECTION_ERROR

class CountryFragment : Fragment(), OnCountryClickListener {
    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<CountryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCountries()
        viewModel.getCountryState().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: CountryState) {
        when (state) {
            is CountryState.Loading -> {
                showLoading()
            }

            is CountryState.Error -> {
                if (state.errorCode == NETWORK_CONNECTION_ERROR) {
                    showInternetConnectionError()
                } else {
                    showError()
                }
            }

            is CountryState.Content -> {
                showContent(state.data)
            }
        }
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.listCountries.visibility = View.GONE
    }

    private fun showInternetConnectionError() {
        binding.progress.visibility = View.GONE
        binding.listCountries.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_connection_error)
        binding.textPlaceholder.text = getString(R.string.connection_error)
        binding.containerPlaceholder.visibility = View.VISIBLE
    }

    private fun showError() {
        binding.progress.visibility = View.GONE
        binding.listCountries.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_search_error)
        binding.textPlaceholder.text = getString(R.string.failed_to_get_country_list)
        binding.containerPlaceholder.visibility = View.VISIBLE
    }

    private fun showContent(data: List<Area>) {
        binding.progress.visibility = View.GONE
        binding.listCountries.adapter = CountryAdapter(mapAreaListToCountryList(data), this)
        binding.listCountries.visibility = View.VISIBLE
    }

    private fun mapAreaListToCountryList(areas: List<Area>): List<Country> {
        val convertor = AreaConverter()
        var countryList = mutableListOf<Country>()
        for (item in areas) {
            countryList.add(convertor.mapToCountry(item))
        }
        return countryList
    }

    override fun onCountryClick(country: Country) {
        val countryJson = Gson().toJson(country)
        parentFragmentManager.setFragmentResult(
            SENDING_DATA_KEY,
            bundleOf(COUNTRY to countryJson)
        )
        findNavController().navigateUp()
    }

    companion object {
        private const val SENDING_DATA_KEY = "sendingDataKey"
        private const val COUNTRY = "country"
    }
}
