package ru.practicum.android.diploma.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.NETWORK_CONNECTION_ERROR

class SearchFragment : Fragment(), OnVacancyClickListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SearchViewModel>()

    private var adapter: VacancyAdapter? = null

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

        adapter = VacancyAdapter(this, viewLifecycleOwner.lifecycleScope)
        binding.listVacancies.adapter = adapter

        binding.listVacancies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos = (binding.listVacancies.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = recyclerView.adapter?.itemCount ?: 0

                    if (pos >= itemsCount - 1) {
                        viewModel.searchVacancies(binding.inputSearchVacancy.text.toString())
                    }
                }
            }
        })

        binding.icon.setOnClickListener {
            binding.inputSearchVacancy.text.clear()
            viewModel.stopSearch()
            adapter?.clearVacancies()
            showDefaultPicture()
        }

        viewModel.getSearchVacanciesState().observe(viewLifecycleOwner) { _state ->
            if (_state != null) {
                render(_state)
            }
        }

        binding.inputSearchVacancy.setOnEditorActionListener { v, actionId, event ->
            val isDoneOrNext = actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
            val isEnterPressed = event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN

            if (isDoneOrNext || isEnterPressed) {
                adapter?.clearVacancies()
                viewModel.stopSearch()
                viewModel.searchVacancies(binding.inputSearchVacancy.text.toString(), true)
                hideKeyboard()
            }
            false
        }

        binding.inputSearchVacancy.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Метод не используется, но нужен для интерфейса TextWatcher
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
                viewModel.searchDebounce(binding.inputSearchVacancy.text.toString())
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
    }

    override fun onResume() {
        super.onResume()
        viewModel.stopSearch()
    }

    private fun render(state: SearchVacanciesState) {
        when (state) {
            is SearchVacanciesState.Default -> {
                showDefaultPicture()
            }

            is SearchVacanciesState.Loading -> {
                if (adapter?.itemCount?.equals(0) == true) {
                    showProgressBar()
                } else {
                    binding.appendProgress.visibility = View.VISIBLE
                }
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
        binding.appendProgress.visibility = View.GONE
        binding.containerPlaceholder.visibility = View.GONE
        binding.resultSearch.visibility = View.GONE
        binding.listVacancies.visibility = View.GONE
        binding.resultSearch.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE
        hideKeyboard()
    }

    private fun showInternetConnectionError() {
        binding.appendProgress.visibility = View.GONE
        binding.progress.visibility = View.GONE
        if (adapter?.itemCount?.equals(0) == true) {
            binding.resultSearch.visibility = View.GONE
            binding.listVacancies.visibility = View.GONE
            binding.placeholder.setImageResource(R.drawable.img_placeholder_connection_error)
            binding.textPlaceholder.text = getString(R.string.connection_error)
            binding.textPlaceholder.visibility = View.VISIBLE
            binding.containerPlaceholder.visibility = View.VISIBLE
        } else {
            binding.containerPlaceholder.visibility = View.GONE
            binding.resultSearch.visibility = View.VISIBLE
            binding.listVacancies.visibility = View.VISIBLE
            Toast.makeText(requireContext(), getString(R.string.connection_error_1), Toast.LENGTH_LONG).show()
        }
        hideKeyboard()
    }

    private fun showServerError() {
        binding.appendProgress.visibility = View.GONE
        binding.progress.visibility = View.GONE
        if (adapter?.itemCount?.equals(0) == true) {
            binding.resultSearch.visibility = View.GONE
            binding.listVacancies.visibility = View.GONE
            binding.placeholder.setImageResource(R.drawable.img_placeholder_search_server_error)
            binding.textPlaceholder.text = getString(R.string.server_error)
            binding.textPlaceholder.visibility = View.VISIBLE
            binding.containerPlaceholder.visibility = View.VISIBLE
        } else {
            binding.containerPlaceholder.visibility = View.GONE
            binding.resultSearch.visibility = View.VISIBLE
            binding.listVacancies.visibility = View.VISIBLE
            Toast.makeText(requireContext(), getString(R.string.server_error_1), Toast.LENGTH_LONG).show()
        }
        hideKeyboard()
    }

    private fun showFoundVacancies(vacancies: Vacancies) {
        binding.appendProgress.visibility = View.GONE
        binding.progress.visibility = View.GONE
        binding.resultSearch.visibility = View.GONE
        adapter?.updateVacancies(vacancies.items)
        binding.listVacancies.visibility = View.VISIBLE
        binding.containerPlaceholder.visibility = View.GONE
        binding.resultSearch.text = getVacancyCountFormatted(vacancies.found)
        binding.resultSearch.visibility = View.VISIBLE
        hideKeyboard()
    }

    private fun showEmptyResult() {
        binding.appendProgress.visibility = View.GONE
        binding.progress.visibility = View.GONE
        binding.resultSearch.visibility = View.GONE
        binding.listVacancies.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_search_error)
        binding.textPlaceholder.visibility = View.VISIBLE
        binding.textPlaceholder.text = getString(R.string.search_error)
        binding.containerPlaceholder.visibility = View.VISIBLE
        binding.resultSearch.text = getString(R.string.noSuchVacancies)
        binding.resultSearch.visibility = View.VISIBLE
        hideKeyboard()
    }

    private fun showDefaultPicture() {
        binding.appendProgress.visibility = View.GONE
        binding.progress.visibility = View.GONE
        binding.resultSearch.visibility = View.GONE
        binding.listVacancies.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_main)
        binding.textPlaceholder.visibility = View.GONE
        binding.containerPlaceholder.visibility = View.VISIBLE
        binding.placeholder.visibility = View.VISIBLE
        binding.textPlaceholder.visibility = View.GONE
        binding.resultSearch.visibility = View.GONE
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun getVacancyCountFormatted(count: Int): String {
        val lastDigit = count % TEN
        val lastTwoDigits = count % HUNDRED
        return when {
            lastDigit == SINGULAR_DIGIT && lastTwoDigits != SINGULAR_EXCEPTION -> "Найдена $count вакансия"
            lastDigit in FEW_DIGIT_RANGE && lastTwoDigits !in FEW_EXCEPTION_RANGE -> "Найдено $count вакансии"
            else -> "Найдено $count вакансий"
        }
    }

    override fun onVacancyClick(vacancy: Vacancy) {
        // Нужно реализовать передачу данных в VacancyFragment
        val action = SearchFragmentDirections.actionSearchFragmentToVacancyFragment(vacancy.vacancyId, SEARCH_FRAGMENT)
        findNavController().navigate(action)
    }

    private companion object {

        const val SEARCH_FRAGMENT = "SearchFragment"
        private const val SINGULAR_DIGIT = 1
        private const val SINGULAR_EXCEPTION = 11
        private const val TEN = 10
        private const val HUNDRED = 100
        private val FEW_DIGIT_RANGE = 2..4
        private val FEW_EXCEPTION_RANGE = 12..14
    }
}
