package ru.practicum.android.diploma.ui.region

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.bundle.bundleOf
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.ui.country.AreaConverter
import ru.practicum.android.diploma.util.NETWORK_CONNECTION_ERROR

class RegionFragment : Fragment(), OnRegionClickListener {
    private var _binding: FragmentRegionBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<RegionViewModel>()
    private var country = Country(EMPTY_STRING, EMPTY_STRING)
    private var searchJob: Job? = null
    private var defaultRegionList: List<Region> = emptyList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val countryJson = arguments?.getString(COUNTRY)
        country = Gson().fromJson(countryJson, Country::class.java)

        getRegions(country.countryId)

        viewModel.getRegionState().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.inputSearchRegion.setOnEditorActionListener { v, actionId, event ->
            val isDoneOrNext = actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
            val isEnterPressed = event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN

            if (isDoneOrNext || isEnterPressed) {
                val allItems = defaultRegionList
                if (allItems != null) {
                    val filterResult = getAllRegionsWithNamesContain(
                        binding.inputSearchRegion.text.toString(),
                        allItems
                    )
                    if (filterResult.isNullOrEmpty()) {
                        showNoSuchRegion()
                    } else {
                        showResult(filterResult.map { region ->
                            Area(
                                region.regionId,
                                region.parentId,
                                region.regionName
                            )
                        })
                    }
                }
                hideKeyboard()
            }
            false
        }
        binding.inputSearchRegion.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val editText = v as EditText
                if (event.rawX >= editText.right - editText.compoundPaddingEnd) {
                    editText.text.clear()
                    true
                }
            }
            false
        }
        binding.inputSearchRegion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Метод не используется, но нужен для интерфейса TextWatcher
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotBlank() == true) {
                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_close)
                    drawable?.setTint(ContextCompat.getColor(requireContext(), R.color.yp_black))
                    binding.inputSearchRegion.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                } else {
                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
                    binding.inputSearchRegion.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                    getRegions(country.countryId)
                }

            }

            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(DEBOUNCE_DELAY)
                    val allItems = defaultRegionList
                    if (allItems != null) {
                        val filterResult = getAllRegionsWithNamesContain(s.toString(), allItems)
                        if (filterResult.isNullOrEmpty()) {
                            showNoSuchRegion()
                        } else {
                            showResult(filterResult.map { region ->
                                Area(
                                    region.regionId,
                                    region.parentId,
                                    region.regionName
                                )
                            })
                        }
                    }
                    if (!binding.inputSearchRegion.text.isNullOrEmpty()) {
                        hideKeyboard()
                    }
                }
            }
        })
    }

    private fun render(state: RegionState) {
        when (state) {
            is RegionState.Loading -> {
                showLoading()
            }

            is RegionState.Error -> {
                if (state.errorCode == NETWORK_CONNECTION_ERROR) {
                    showInternetConnectionError()
                } else {
                    showError()
                }
            }

            is RegionState.Content -> {
                showResult(state.areas)
                defaultRegionList = state.areas.map { area -> Region(area.id, area.parentId, area.name) }
            }
        }
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.listRegions.visibility = View.GONE
        binding.containerPlaceholder.visibility = View.GONE
    }

    private fun showInternetConnectionError() {
        binding.progress.visibility = View.GONE
        binding.listRegions.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_connection_error)
        binding.textPlaceholder.text = getString(R.string.connection_error)
        binding.containerPlaceholder.visibility = View.VISIBLE
    }

    private fun showError() {
        binding.progress.visibility = View.GONE
        binding.listRegions.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_region_error)
        binding.textPlaceholder.text = getString(R.string.failed_to_get_regions)
        binding.containerPlaceholder.visibility = View.VISIBLE
    }

    private fun showNoSuchRegion() {
        binding.progress.visibility = View.GONE
        binding.listRegions.visibility = View.GONE
        binding.placeholder.setImageResource(R.drawable.img_placeholder_search_error)
        binding.textPlaceholder.text = getString(R.string.no_such_region)
        binding.containerPlaceholder.visibility = View.VISIBLE
    }

    private fun showResult(areas: List<Area>) {
        binding.progress.visibility = View.GONE
        binding.listRegions.adapter = RegionAdapter(mapAreaListToRegionList(areas), this)
        binding.listRegions.visibility = View.VISIBLE
        binding.containerPlaceholder.visibility = View.GONE
    }

    private fun getAllRegionsWithNamesContain(query: String, list: List<Region>): List<Region> {
        return list.filter { it.regionName.contains(query.trim(), ignoreCase = true) }
    }

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun mapAreaListToRegionList(areas: List<Area>): List<Region> {
        val convertor = AreaConverter()
        var areaList = mutableListOf<Region>()
        for (item in areas) {
            areaList.add(convertor.mapToRegion(item))
        }
        return areaList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRegionClick(region: Region) {
        val regionJson = Gson().toJson(region)
        parentFragmentManager.setFragmentResult(
            SENDING_DATA_KEY,
            bundleOf(REGION to regionJson)
        )
        findNavController().navigateUp()
    }

    private fun getRegions(countryId: String?) {
        if (countryId.isNullOrEmpty()) {
            viewModel.getAllRegions()
        } else {
            viewModel.getRegions(countryId)
        }
    }

    private companion object {
        const val SENDING_DATA_KEY = "sendingDataKey"
        const val REGION = "region"
        const val COUNTRY = "country"
        const val DEBOUNCE_DELAY = 2000L
        const val EMPTY_STRING = ""
    }
}
