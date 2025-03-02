package ru.practicum.android.diploma.ui.country

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.databinding.FragmentCountryBinding
import ru.practicum.android.diploma.domain.models.Country

class CountryFragment : Fragment(), OnCountryClickListener {
    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!

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

        binding.listCountries.adapter = CountryAdapter(getCountryListForTest(), this)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Тестовый метод для генерации данных
    private fun getCountryListForTest(): List<Country> {
        var list = mutableListOf<Country>()
        list.add(Country("1", "Россия"))
        list.add(Country("2", "Украина"))
        list.add(Country("3", "Белорусь"))
        list.add(Country("4", "Казахстан"))
        return list
    }

    override fun onCountryClick(country: Country) {
        val navController = findNavController()
        val currentBackStackEntry = navController.previousBackStackEntry
        val args = currentBackStackEntry?.arguments

        val regionId = args?.getString("regionId") ?: ""

        val action = CountryFragmentDirections
            .actionCountryFragmentToWorkplaceFragment(country.countryId, regionId)

        navController.navigate(action)
    }
}
