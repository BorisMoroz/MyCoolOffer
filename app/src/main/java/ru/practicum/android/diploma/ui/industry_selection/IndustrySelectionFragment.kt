package ru.practicum.android.diploma.ui.industry_selection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentIndustrySelectionBinding

class IndustrySelectionFragment : Fragment() {
    private lateinit var binding: FragmentIndustrySelectionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIndustrySelectionBinding.inflate(inflater, container, false)
        return binding.root
    }
}
