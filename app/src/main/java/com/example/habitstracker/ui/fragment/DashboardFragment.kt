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
import com.example.habitstracker.data.repository.HealthRecordRepository
import com.example.habitstracker.data.repository.UserRepository
import com.example.habitstracker.data.repository.WorkoutRepository
import com.example.habitstracker.viewmodel.HealthRecordViewModel
import com.example.habitstracker.viewmodel.HealthRecordViewModelFactory
import com.example.habitstracker.viewmodel.UserViewModel
import com.example.habitstracker.viewmodel.UserViewModelFactory
import com.example.habitstracker.viewmodel.WorkoutViewModel
import com.example.habitstracker.viewmodel.WorkoutViewModelFactory
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
    private val userViewModel: UserViewModel by viewModels {

        UserViewModelFactory(

            UserRepository(

                HabitsTrackerDatabase
                    .getInstance(requireContext())
                    .userDao()
            )
        )
    }

    private val workoutViewModel: WorkoutViewModel by viewModels {

        WorkoutViewModelFactory(

            WorkoutRepository(

                HabitsTrackerDatabase
                    .getInstance(requireContext())
                    .workoutDao()
            )
        )
    }

    private val healthViewModel: HealthRecordViewModel by viewModels {
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

        _binding = FragmentDashboardBinding.bind(view)

        observeUser()

        observeDashboard()

        observeWorkout()

        observeHealth()
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

    private fun observeUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.currentUser.collect { user ->
                if (user != null) {
                    binding.tvWelcome.text = "Welcome, ${user.name}"
                } else {
                    binding.tvWelcome.text = "Welcome"
                }
            }
        }
    }

    private fun observeWorkout() {

        viewLifecycleOwner.lifecycleScope.launch {

            workoutViewModel
                .latestWorkoutSummary
                .collect { workout ->

                    if (workout == null) {
                        binding.tvWorkout.text =
                            "Latest Workout\nNo workout"

                    } else {

                        binding.tvWorkout.text =
                            """
                        Latest Workout
                        Type : ${workout.name}                            
                        Date : ${workout.date}                           
                        Duration : ${workout.duration} min                  
                        Calories : ${workout.calories} kcal
                        """.trimIndent()

                    }
                }
        }
    }

    private fun observeHealth() {
        viewLifecycleOwner.lifecycleScope.launch {
            healthViewModel.latestHealth.collect { health ->
                if (health == null) {
                    binding.tvHealth.text = "Latest Health\nNo record"
                } else {
                    binding.tvHealth.text =

                        """
                    Latest Health
                    Weight : ${health.weight} kg
                    Water : ${health.waterIntake} L
                    Sleep : ${health.sleepHours} h
                    Heart : ${health.heartRate} bpm
                    """.trimIndent()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}