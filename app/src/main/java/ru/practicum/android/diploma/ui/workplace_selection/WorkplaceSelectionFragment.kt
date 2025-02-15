package ru.practicum.android.diploma.ui.workplace_selection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentWorkplaceSelectionBinding

class WorkplaceSelectionFragment : Fragment() {
    private lateinit var binding: FragmentWorkplaceSelectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkplaceSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }
}
