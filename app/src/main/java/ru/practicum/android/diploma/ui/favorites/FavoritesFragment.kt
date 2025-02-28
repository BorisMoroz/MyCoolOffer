package ru.practicum.android.diploma.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FavouritesViewModel>()
    private val favouritesNotEmptyList = FavouritesVacancyState.Success
    private val favouritesEmptyList = FavouritesVacancyState.Empty
    private var _adapter: FavouritesVacancyAdapter? = null
    private val adapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFavouritesFragmentObservers()
        setRecyclerView()
        viewModel.checkVacancyList()

        adapter.onFavouriteVacancyClick = {
            val action =
                FavoritesFragmentDirections.actionFavoritesFragmentToVacancyFragment(it.vacancyId, FAVOURITES_FRAGMENT)
            findNavController().navigate(action)
        }
    }

    private fun setUpFavouritesFragmentObservers() {
        viewModel.getVacancyListState().observe(viewLifecycleOwner) { state ->
            handleVacancyListState(state)
        }
        viewModel.getVacancyList().observe(viewLifecycleOwner) { vacancyList ->
            handleVacancyList(vacancyList)
        }
    }

    private fun handleVacancyListState(state: FavouritesVacancyState) {
        when (state) {
            favouritesEmptyList -> showEmptyListPlaceholder()
            favouritesNotEmptyList -> showVacancyList()
            else -> showErrorPlaceholder()
        }
    }

    private fun handleVacancyList(vacancyList: List<Vacancy>) {
        adapter.data = vacancyList
        adapter.notifyDataSetChanged()
    }

    private fun setRecyclerView() {
        _adapter = FavouritesVacancyAdapter()
        binding.listVacancies.layoutManager = LinearLayoutManager(requireContext())
        binding.listVacancies.adapter = _adapter
    }

    private fun showEmptyListPlaceholder() {
        binding.apply {
            favouritesPlaceholder.setImageResource(R.drawable.img_placeholder_favourites_error)
            favouritesPlaceholderText.text = getString(R.string.empty_favourite_list)
            favouritesContainerPlaceholder.isVisible = true
            listVacancies.isGone = true
        }
    }

    private fun showVacancyList() {
        binding.apply {
            favouritesContainerPlaceholder.isVisible = false
            listVacancies.isVisible = true
        }
    }

    private fun showErrorPlaceholder() {
        binding.apply {
            favouritesPlaceholder.setImageResource(R.drawable.img_placeholder_search_error)
            favouritesPlaceholderText.text = getString(R.string.search_error)
            favouritesContainerPlaceholder.isVisible = true
            listVacancies.isGone = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    private companion object {
        const val FAVOURITES_FRAGMENT = "FavouritesFragment"
    }
}
