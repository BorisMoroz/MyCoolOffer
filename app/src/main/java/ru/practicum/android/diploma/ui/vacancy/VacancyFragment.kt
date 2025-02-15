package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding

class VacancyFragment : Fragment() {
    private lateinit var binding: FragmentVacancyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }
}
