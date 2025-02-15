package ru.practicum.android.diploma.ui.vacancy_search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentVacancySearchBinding

class VacancySearchFragment : Fragment() {
    private lateinit var binding: FragmentVacancySearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVacancySearchBinding.inflate(inflater, container, false)
        return binding.root
    }
}
