package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
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

        viewModel.getSearchVacanciesState().observe(viewLifecycleOwner) { _state ->
            if (_state != null) {
                render(_state)
            }
        }

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
                // Нужно реализовать отображение успешного результата поиска
            }
        }
    }

    private fun showProgressBar() {
        binding.progress.visibility = View.VISIBLE
        binding.resultSearch.visibility = View.GONE
        binding.listVacancies.visibility = View.GONE
        binding.containerPlaceholder.visibility = View.GONE
    }

    private fun showInternetConnectionError() {
        binding.progress.visibility = View.GONE
        binding.resultSearch.visibility = View.GONE
        binding.listVacancies.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_connection_error)
        binding.textPlaceholder.text = getString(R.string.connection_error)
        binding.containerPlaceholder.visibility = View.VISIBLE
    }

    private fun showServerError() {
        binding.progress.visibility = View.GONE
        binding.resultSearch.visibility = View.GONE
        binding.listVacancies.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_search_server_error)
        binding.textPlaceholder.text = getString(R.string.server_error)
        binding.containerPlaceholder.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
