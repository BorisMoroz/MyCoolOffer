package ru.practicum.android.diploma.ui.region

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.bundle.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.domain.models.Region

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Метод для генерации данных
    private fun getRegionListForTest(): List<Region> {
        var list = mutableListOf<Region>()
        list.add(Region("1", "Москва"))
        list.add(Region("2", "Санкт-Петербург"))
        list.add(Region("3", "Кисловодск"))
        list.add(Region("4", "Рязань"))
        return list
    }

    override fun onRegionClick(region: Region) {
        parentFragmentManager.setFragmentResult(
            SENDING_DATA_KEY,
            bundleOf(REGION_ID_KEY to region.regionId)
        )
        findNavController().navigateUp()
    }

    companion object {
        private const val SENDING_DATA_KEY = "sendingDataKey"
        private const val REGION_ID_KEY = "regionIdKey"
    }
}
