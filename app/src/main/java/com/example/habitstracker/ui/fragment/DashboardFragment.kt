package com.example.habitstracker.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.habitstracker.R
import com.example.habitstracker.data.database.HabitsTrackerDatabase
import com.example.habitstracker.data.repository.HabitRepository
import com.example.habitstracker.databinding.FragmentDashboardBinding
import com.example.habitstracker.utils.StreakUtils
import com.example.habitstracker.viewmodel.HabitViewModel
import com.example.habitstracker.viewmodel.HabitViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDate

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(
            HabitRepository(
                HabitsTrackerDatabase
                    .getInstance(requireContext())
                    .habitDao()
            )
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDashboardBinding.bind(view)

        observeDashboard()
    }

    private fun observeDashboard() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.habitsWithLogs.collect { habits ->

                val today = LocalDate.now().toString()

                val totalHabit = habits.size

                val completedToday = habits.count { relation ->
                    relation.logs.any {
                        it.date == today && it.completed
                    }
                }

                val progress =
                    if (totalHabit == 0) 0
                    else completedToday * 100 / totalHabit

                val longestStreak =
                    habits.maxOfOrNull {
                        StreakUtils.calculateStreak(it.logs)
                    } ?: 0

                binding.tvTotalHabit.text = "Total Habit: $totalHabit"

                binding.tvCompleted.text = "Completed Today: $completedToday"

                binding.tvProgress.text = "Progress: $progress%"

                binding.tvLongestStreak.text = "🔥 Longest Streak: $longestStreak day(s)"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}