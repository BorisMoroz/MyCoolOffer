package ru.practicum.android.diploma.ui.vacancy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.NETWORK_CONNECTION_ERROR
import ru.practicum.android.diploma.util.NOT_FOUND_ERROR

class VacancyFragment : Fragment() {
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private var isChecked = false
    private var url: String? = null
    private val vacancyArgs by navArgs<VacancyFragmentArgs>()
    private val viewModel by viewModel<VacancyViewModel>()
    private var _vacancyDetails: VacancyDetails? = null
    private val vacancyDetails get() = _vacancyDetails!!

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
        setUpVacancyFragmentObservers()

        val vacancyId = vacancyArgs.vacancyId
        val fragment = vacancyArgs.fragment

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_share -> {
                    url?.let { shareVacancy(it) }
                    true
                }

                R.id.action_like -> {
                    changeLikeButtonStatus(isChecked, vacancyDetails)
                    true
                }

                else -> false
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        when (fragment) {
            SEARCH_FRAGMENT -> {
                viewModel.getVacancyDetails(vacancyId)
                setVacancyFragmentObservers()
                viewModel.checkVacancyInFavouriteList(vacancyId)
            }

            FAVOURITES_FRAGMENT -> {
                viewModel.getVacancyDetailsFromDb(vacancyId)
                setVacancyFromDbObservers()
                viewModel.checkVacancyInFavouriteList(vacancyId)
            }
        }
    }

    private fun setVacancyFragmentObservers() {
        viewModel.getVacancyDetailsState().observe(viewLifecycleOwner) { vacancyDetailsState ->
            when (vacancyDetailsState) {
                is VacancyDetailsState.Loading -> renderLoading()
                is VacancyDetailsState.Content -> {
                    renderContent(vacancyDetailsState.data)
                    _vacancyDetails = vacancyDetailsState.data
                }

                is VacancyDetailsState.Error -> renderError(vacancyDetailsState.errorCode)
                else -> {}
            }
        }
    }

    private fun setVacancyFromDbObservers() {
        viewModel.getVacancyFromDb().observe(viewLifecycleOwner) { vacancyFromDb ->
            renderContent(vacancyFromDb)
            _vacancyDetails = vacancyFromDb
        }
    }

    private fun renderLoading() {
        binding.progress.isVisible = true
        binding.vacancyContent.isVisible = false
        binding.placeholderError.isVisible = false
    }

    private fun renderContent(vacancyDetails: VacancyDetails) {
        binding.progress.isVisible = false
        binding.vacancyContent.isVisible = true
        binding.placeholderError.isVisible = false
        bindVacancyDetails(vacancyDetails)
    }

    private fun renderError(errorCode: Int) {
        binding.progress.isVisible = false
        binding.vacancyContent.isVisible = false
        binding.toolbar.menu.clear()
        when (errorCode) {
            NETWORK_CONNECTION_ERROR -> {
                Glide.with(this)
                    .load(R.drawable.img_placeholder_connection_error)
                    .into(binding.placeholderErrorImage)
                binding.placeholderErrorText.setText(R.string.connection_error)
            }

            NOT_FOUND_ERROR -> {
                Glide.with(this)
                    .load(R.drawable.img_placeholder_job_error)
                    .into(binding.placeholderErrorImage)
                binding.placeholderErrorText.setText(R.string.vacancy_error)
            }

            else -> {
                Glide.with(this)
                    .load(R.drawable.img_placeholder_job_server_error)
                    .into(binding.placeholderErrorImage)
                binding.placeholderErrorText.setText(R.string.server_error)
            }
        }
        binding.placeholderError.isVisible = true
    }

    private fun bindVacancyDetails(vacancyDetails: VacancyDetails) {
        viewModel.setSalaryTextValues(
            salaryFromTextValue = getString(R.string.from_text),
            salaryToTextValue = getString(R.string.to_text),
            defaultSalaryTextValue = getString(R.string.default_salary_text)
        )
        url = URL + vacancyDetails.vacancyId
        binding.nameText.text = vacancyDetails.vacancyName
        binding.salaryText.text = viewModel.getSalaryText(
            salaryFrom = vacancyDetails.salaryFrom,
            salaryTo = vacancyDetails.salaryTo,
            currency = vacancyDetails.currency,
        )
        loadVacancyCover(vacancyDetails.logoUrl)
        binding.vacancyCardEmployerText.text = vacancyDetails.employer
        binding.vacancyCardEmployerText.isSelected = true
        if (!vacancyDetails.address.isNullOrEmpty()) {
            binding.vacancyCardRegionText.text = vacancyDetails.address
        } else {
            binding.vacancyCardRegionText.text = vacancyDetails.area
        }
        binding.vacancyCardRegionText.isSelected = true
        binding.experienceText.text = vacancyDetails.experience
        binding.workFormatText.text = viewModel.getWorkFormatText(vacancyDetails.workFormat, vacancyDetails.employment)
        binding.vacancyDescriptionText.text = vacancyDetails.description?.let {
            HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).trimEnd()
        }
        if (!vacancyDetails.keySkills.isNullOrEmpty()) {
            binding.skillsText.text = viewModel.getSkillsText(
                skills = vacancyDetails.keySkills,
                keySkillsNewLine = getString(R.string.key_skills_new_line)
            )
        } else {
            binding.skillsLayout.isVisible = false
        }
    }

    private fun shareVacancy(url: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(shareIntent)
    }

    private fun setUpVacancyFragmentObservers() {
        viewModel.getIsVacancyFavouriteState().observe(viewLifecycleOwner) { isFavourite ->
            handleIsVacancyFavourite(isFavourite)
        }
    }

    private fun handleIsVacancyFavourite(isFavourite: Boolean) {
        val likeButton = binding.toolbar.menu.findItem(R.id.action_like)
        if (isFavourite) {
            likeButton.setIcon(R.drawable.ic_favourite_like)
            isChecked = true
        } else {
            likeButton.setIcon(R.drawable.ic_favourite_off)
            isChecked = false
        }
    }

    // Тестовая функция смены иконки избранной вакансии
    private fun changeLikeButtonStatus(value: Boolean, vacancy: VacancyDetails) {
        val likeButton = binding.toolbar.menu.findItem(R.id.action_like)
        if (value) {
            likeButton.setIcon(R.drawable.ic_favourite_off)
            isChecked = false
            viewModel.removeVacancyFromFavourites(vacancy)
        } else {
            likeButton.setIcon(R.drawable.ic_favourite_like)
            isChecked = true
            viewModel.addVacancyToFavourites(vacancy)
        }
    }

    private fun loadVacancyCover(coverUrl: String?) {
        Glide.with(this)
            .load(coverUrl)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .transform(
                CenterCrop(),
                RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.radius_12))
            )
            .placeholder(R.drawable.vacancy_placeholder)
            .into(binding.vacancyCardImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _vacancyDetails = null
    }

    private companion object {
        const val SEARCH_FRAGMENT = "SearchFragment"
        const val FAVOURITES_FRAGMENT = "FavouritesFragment"
        const val URL = "https://hh.ru/vacancy/"
    }

}
