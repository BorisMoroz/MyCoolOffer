package ru.practicum.android.diploma.ui.filter_settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding

class FilterSettingsFragment : Fragment() {
    private lateinit var binding: FragmentFilterSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
}
