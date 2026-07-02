package com.example.habitstracker.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitstracker.R
import com.example.habitstracker.data.database.HabitsTrackerDatabase
import com.example.habitstracker.data.entity.Habit
import com.example.habitstracker.data.repository.HabitRepository
import com.example.habitstracker.databinding.FragmentHabitBinding
import com.example.habitstracker.ui.adapter.HabitAdapter
import com.example.habitstracker.viewmodel.HabitViewModel
import com.example.habitstracker.viewmodel.HabitViewModelFactory
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog
import com.example.habitstracker.ui.model.HabitItem
import com.example.habitstracker.data.entity.HabitLog
import com.example.habitstracker.utils.StreakUtils



class HabitFragment : Fragment(R.layout.fragment_habit) {

    private var _binding: FragmentHabitBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HabitAdapter

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

        _binding = FragmentHabitBinding.bind(view)

        adapter = HabitAdapter(

            onItemClick = { habit ->

                val fragment = EditHabitFragment()

                val bundle = Bundle()

                bundle.putLong(
                    "habitId",
                    habit.habitId
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

            onDeleteClick = { habit ->

                AlertDialog.Builder(requireContext())
                    .setTitle("Xóa Habit")
                    .setMessage(
                        "Bạn có chắc muốn xóa ${habit.name}?"
                    )
                    .setPositiveButton("Có") { _, _ ->

                        viewModel.deleteHabit(habit)
                    }
                    .setNegativeButton("Không", null)
                    .show()
            },

            onCheckedChanged = { habit, checked ->
                val today = java.time.LocalDate.now().toString()
                val currentTime = java.time.LocalTime.now().toString()
                val log = HabitLog(

                    habitId = habit.habitId,
                    date = today,
                    progress = habit.target,
                    completed = checked,
                    completedTime = if (checked) currentTime else null
                )

                viewModel.insertOrUpdateHabitLog(log)
            }
        )

        binding.rvHabits.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvHabits.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycleScope.launch {

                viewModel.habitsWithLogs.collect { list ->

                    val today =
                        java.time.LocalDate.now().toString()

                    val items = list.map { relation ->

                        HabitItem(

                            habit = relation.habit,

                            completedToday = relation.logs.any {

                                it.date == today &&
                                        it.completed
                            },
                            streak =
                            StreakUtils.calculateStreak(
                                relation.logs
                            )
                        )
                    }

                    adapter.submitList(items)
                }
            }
        }


        binding.btnAddHabit.setOnClickListener {

            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    AddHabitFragment()
                )
                .addToBackStack(null)
                .commit()
        }
        binding.btnReminder.setOnClickListener {

            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    ReminderFragment()
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