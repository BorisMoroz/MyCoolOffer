package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding

class VacancyFragment : Fragment() {
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private var isChecked = false

    private val viewModel by viewModel<VacancyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_share -> {
                    // Реализовать логику "Поделиться вакансией"
                    Log.d("log", "Share button clicked")
                    true
                }
                R.id.action_like -> {
                    changeLikeButtonStatus(isChecked)
                    // Реализовать добавление вакансии в избранное
                    Log.d("log", "Like button clicked")
                    true
                }
                else -> false
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

    }

    // Тестовая функция смены иконки избранной вакансии
    private fun changeLikeButtonStatus(value: Boolean) {
        val likeButton = binding.toolbar.menu.findItem(R.id.action_like)
        if (value) {
            likeButton.setIcon(R.drawable.ic_favourite_off)
            isChecked = false
        } else {
            likeButton.setIcon(R.drawable.ic_favourite_like)
            isChecked = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
