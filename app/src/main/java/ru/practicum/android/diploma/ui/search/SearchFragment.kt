package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.util.NETWORK_CONNECTION_ERROR

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icon.setImageResource(R.drawable.ic_search)
        binding.icon.isVisible = true

        binding.icon.setOnClickListener {
            binding.inputSearchVacancy.text.clear()
        }

        viewModel.getSearchVacanciesState().observe(viewLifecycleOwner) { _state ->
            if (_state != null) {
                render(_state)
            }
        }

        binding.inputSearchVacancy.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_NEXT ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                viewModel.stopSearch()
                // Нужно настроить пагинацию
                viewModel.searchVacancies(binding.inputSearchVacancy.text.toString(), 1, 20)
            }
            false
        }

        binding.inputSearchVacancy.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotBlank() == true) {
                    binding.icon.setImageResource(R.drawable.ic_close)
                    binding.icon.isVisible = true
                } else {
                    binding.icon.setImageResource(R.drawable.ic_search)
                    binding.icon.isVisible = true
                }

            }

            override fun afterTextChanged(s: Editable?) {
                // Нужно настроить пагинацию
                viewModel.searchDebounce(binding.inputSearchVacancy.text.toString(), 1, 20)
            }
        })

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_filter -> {
                    findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToFilterFragment())
                    true
                }

                else -> false
            }
        }
//        пока не нужна
//        binding.resultSearch.setOnClickListener {
//            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToVacancyFragment())
//        }

    }

    private fun render(state: SearchVacanciesState) {
        when (state) {
            is SearchVacanciesState.Default -> {
                showDefaultPicture()
            }

            is SearchVacanciesState.Loading -> {
                showProgressBar()
            }

            is SearchVacanciesState.Error -> {
                if (state.errorCode == NETWORK_CONNECTION_ERROR) {
                    showInternetConnectionError()
                } else {
                    showServerError()
                }
            }

            is SearchVacanciesState.Content -> {
                if (state.data.items.isNullOrEmpty()) {
                    showEmptyResult()
                } else {
                    showFoundVacancies(state.data)
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.progress.visibility = View.VISIBLE
        binding.resultSearch.visibility = View.GONE
        binding.listVacancies.visibility = View.GONE
        binding.containerPlaceholder.visibility = View.GONE
        binding.resultSearch.visibility = View.GONE
    }

    private fun showInternetConnectionError() {
        binding.progress.visibility = View.GONE
        binding.resultSearch.visibility = View.GONE
        binding.listVacancies.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_connection_error)
        binding.textPlaceholder.text = getString(R.string.connection_error)
        binding.containerPlaceholder.visibility = View.VISIBLE
        binding.resultSearch.visibility = View.GONE
    }

    private fun showServerError() {
        binding.progress.visibility = View.GONE
        binding.resultSearch.visibility = View.GONE
        binding.listVacancies.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_search_server_error)
        binding.textPlaceholder.text = getString(R.string.server_error)
        binding.containerPlaceholder.visibility = View.VISIBLE
        binding.resultSearch.visibility = View.GONE
    }

    private fun showFoundVacancies(vacancies: Vacancies) {
        binding.progress.visibility = View.GONE
        binding.resultSearch.visibility = View.GONE
        binding.listVacancies.adapter = VacancyAdapter(vacancies)
        binding.listVacancies.visibility = View.VISIBLE
        binding.containerPlaceholder.visibility = View.GONE
        binding.resultSearch.text = vacancies.items.size.toString()
        binding.resultSearch.visibility = View.VISIBLE
    }

    private fun showEmptyResult() {
        binding.progress.visibility = View.GONE
        binding.resultSearch.visibility = View.GONE
        binding.listVacancies.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_search_error)
        binding.textPlaceholder.text = getString(R.string.search_error)
        binding.containerPlaceholder.visibility = View.VISIBLE
        binding.resultSearch.text = getString(R.string.noSuchVacancies)
        binding.resultSearch.visibility = View.VISIBLE
    }

    private fun showDefaultPicture() {
        binding.progress.visibility = View.GONE
        binding.resultSearch.visibility = View.GONE
        binding.listVacancies.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_main)
        binding.textPlaceholder.text = ""
        binding.containerPlaceholder.visibility = View.VISIBLE
        binding.resultSearch.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

