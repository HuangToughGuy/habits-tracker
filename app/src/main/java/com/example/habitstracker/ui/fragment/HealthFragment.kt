package com.example.habitstracker.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitstracker.R
import com.example.habitstracker.data.database.HabitsTrackerDatabase
import com.example.habitstracker.data.repository.HealthRecordRepository
import com.example.habitstracker.databinding.FragmentHealthBinding
import com.example.habitstracker.ui.adapter.HealthAdapter
import com.example.habitstracker.viewmodel.HealthRecordViewModel
import com.example.habitstracker.viewmodel.HealthRecordViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HealthFragment : Fragment(R.layout.fragment_health) {

    private var _binding: FragmentHealthBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HealthAdapter

    private val viewModel: HealthRecordViewModel by viewModels {

        HealthRecordViewModelFactory(

            HealthRecordRepository(

                HabitsTrackerDatabase
                    .getInstance(requireContext())
                    .healthRecordDao()
            )
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHealthBinding.bind(view)

        binding.rvHealth.layoutManager =
            LinearLayoutManager(requireContext())

        lifecycleScope.launch {

            val user = HabitsTrackerDatabase
                .getInstance(requireContext())
                .userDao()
                .getCurrentUser()
                .first()
            val height = user?.height ?: 170f
            adapter = HealthAdapter(
                userHeight = height,
                onItemClick = { record ->

                    val fragment = EditHealthFragment()

                    val bundle = Bundle()

                    bundle.putLong(
                        "recordId",
                        record.recordId
                    )
                    fragment.arguments = bundle

                    parentFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.fragmentContainer,
                            fragment
                        )
                        .addToBackStack(null)
                        .commit()
                },

                onDeleteClick = { record ->

                    viewModel.deleteRecord(record)
                }
            )

            binding.rvHealth.adapter = adapter

            viewModel.records.collect { records ->

                adapter.submitList(records)
            }
        }

        binding.btnAddHealth.setOnClickListener {

            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    AddHealthFragment()
                )
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}