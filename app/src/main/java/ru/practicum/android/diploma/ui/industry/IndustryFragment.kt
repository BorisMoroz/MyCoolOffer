package ru.practicum.android.diploma.ui.industry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.ui.search.SearchViewModel

class IndustryFragment : Fragment() {
    private var _binding: FragmentIndustryBinding? = null
    private val binding get() = _binding!!

    private lateinit var industryAdapter: IndustryAdapter

    private val viewModel by viewModel<IndustryViewModel>()


    //var industries = mutableListOf<Industry>()


    //var industriesWithMark = listOf<IndustryWithMark>()

    var industriesWithMark = mutableListOf<IndustryWithMark>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        industryAdapter = IndustryAdapter(industriesWithMark, onChoosedIndustry)

        binding.industriesList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.industriesList.adapter = industryAdapter


        viewModel.getIndustries()

        viewModel.getGetIndustriesState().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }















        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


        /* binding.button2.setOnClickListener {


             viewModel.getIndustries()


            // findNavController().navigateUp()
         }*/
    }



    val onChoosedIndustry: (selectedPosition: Int) -> Unit = { selectedPosition ->


        binding.buttonApply.isVisible = true



    }


    fun renderState(state: GetIndustriesState?) {

        when (state) {
            is GetIndustriesState.Error -> showError(state.errorCode)
            is GetIndustriesState.Content -> showIndustries(state.data)
            //is SearchTracksState.Loading -> showLoading()
            else -> {
                /*hideSearchResults()
                tracks.clear()*/
            }
        }

    }


    private fun showError(errorCode: Int) {
        industriesWithMark.clear()
        industryAdapter.notifyDataSetChanged()

        binding.buttonApply.isVisible = false
        binding.getIndustriesErrorLayout.isVisible = true
    }


    private fun showIndustries(data: Industries) {

        industriesWithMark.clear()


        for(industry in data.items){


            industriesWithMark.add(IndustryWithMark(industry,false))





        }






        industriesWithMark = data.items.map {


            IndustryWithMark(it, false)


        } as MutableList





       // industries.addAll(data.items)




        industryAdapter.notifyDataSetChanged()

        /* if (tracks.isEmpty()) showMessage(SearchResult.NOTHING)
         else {
             showMessage(SearchResult.OK)
             showSearchResults()
         }*/
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
