package ru.practicum.android.diploma.ui.region

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.ui.country.CountryFragmentDirections

class RegionFragment : Fragment(), OnRegionClickListener {
    private var _binding: FragmentRegionBinding? = null
    private val binding get() = _binding!!

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

        binding.listRegions.adapter = RegionAdapter(getRegionListForTest(), this)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

//        binding.button2.setOnClickListener {
//            findNavController().navigateUp()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Тестовый метод для генерации данных
    private fun getRegionListForTest(): List<Region> {
        var list = mutableListOf<Region>()
        list.add(Region("1", "Москва"))
        list.add(Region("2", "Санкт-Петербург"))
        list.add(Region("3", "Кисловодск"))
        list.add(Region("4", "Рязань"))
        return list
    }

    override fun onRegionClick(region: Region) {
        val navController = findNavController()
        val currentBackStackEntry = navController.previousBackStackEntry
        val args = currentBackStackEntry?.arguments

        val countryId = args?.getString("countryId") ?: ""

        val action = RegionFragmentDirections
            .actionRegionFragmentToWorkplaceFragment(countryId, region.regionId)

        navController.navigate(action)
    }
}
