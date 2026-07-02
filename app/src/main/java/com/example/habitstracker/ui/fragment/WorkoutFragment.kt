package com.example.habitstracker.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitstracker.R
import com.example.habitstracker.data.database.HabitsTrackerDatabase
import com.example.habitstracker.data.repository.WorkoutRepository
import com.example.habitstracker.databinding.FragmentWorkoutBinding
import com.example.habitstracker.ui.adapter.WorkoutAdapter
import com.example.habitstracker.viewmodel.WorkoutViewModel
import com.example.habitstracker.viewmodel.WorkoutViewModelFactory
import kotlinx.coroutines.launch

class WorkoutFragment : Fragment(R.layout.fragment_workout) {

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WorkoutAdapter

    private val viewModel: WorkoutViewModel by viewModels {

        WorkoutViewModelFactory(
            WorkoutRepository(
                HabitsTrackerDatabase
                    .getInstance(requireContext())
                    .workoutDao()
            )
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentWorkoutBinding.bind(view)

        adapter = WorkoutAdapter(

            onItemClick = { workout ->

                val fragment = EditWorkoutFragment()

                fragment.arguments = Bundle().apply {

                    putLong(
                        "logId",
                        workout.logId
                    )
                }

                parentFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.fragmentContainer,
                        fragment
                    )
                    .addToBackStack(null)
                    .commit()
            },

            onDeleteClick = {

                viewModel.deleteWorkoutLog(it)
            }
        )

        binding.rvWorkout.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvWorkout.adapter = adapter

        lifecycleScope.launch {

            viewModel.workoutLogs.collect {

                adapter.submitList(it)
            }
        }

        binding.btnAddWorkout.setOnClickListener {

            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    AddWorkoutFragment()
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