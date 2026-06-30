package com.example.habitstracker.ui.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.habitstracker.R
import com.example.habitstracker.data.database.HabitsTrackerDatabase
import com.example.habitstracker.data.entity.Reminder
import com.example.habitstracker.data.repository.HabitRepository
import com.example.habitstracker.data.repository.ReminderRepository
import com.example.habitstracker.databinding.FragmentReminderBinding
import com.example.habitstracker.viewmodel.HabitViewModel
import com.example.habitstracker.viewmodel.HabitViewModelFactory
import com.example.habitstracker.viewmodel.ReminderViewModel
import com.example.habitstracker.viewmodel.ReminderViewModelFactory
import com.example.habitstracker.ui.adapter.ReminderAdapter
import kotlinx.coroutines.launch
import java.util.Calendar
import com.example.habitstracker.alarm.ReminderAlarmManager

class ReminderFragment : Fragment(R.layout.fragment_reminder) {

    private lateinit var adapter: ReminderAdapter
    private var _binding: FragmentReminderBinding? = null
    private val binding get() = _binding!!

    private var selectedHabitId: Long = -1

    private var selectedTime = "08:00"

    private val reminderViewModel: ReminderViewModel by viewModels {

        ReminderViewModelFactory(

            ReminderRepository(

                HabitsTrackerDatabase
                    .getInstance(requireContext())
                    .reminderDao()
            )
        )
    }

    private val habitViewModel: HabitViewModel by viewModels {

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

        _binding = FragmentReminderBinding.bind(view)

        adapter = ReminderAdapter(

            onItemClick = { reminder ->

                val fragment = EditReminderFragment()

                fragment.arguments = Bundle().apply {

                    putLong(
                        "reminderId",
                        reminder.reminderId
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

                reminderViewModel.deleteReminder(it)
            },

            onEnableChanged = { reminder, enabled ->

                reminderViewModel.updateReminder(

                    reminder.copy(

                        enabled = enabled
                    )
                )
            }
        )

        binding.rvReminders.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())

        binding.rvReminders.adapter = adapter

        setupRepeatSpinner()

        loadHabitSpinner()

        observeReminder()

        binding.btnPickTime.setOnClickListener {

            showTimePicker()
        }

        binding.btnSaveReminder.setOnClickListener {

            saveReminder()
        }
    }

    private fun setupRepeatSpinner() {

        val repeats = listOf(

            "Daily",

            "Weekly",

            "Monthly"
        )

        binding.spRepeat.adapter =

            ArrayAdapter(

                requireContext(),

                android.R.layout.simple_spinner_dropdown_item,

                repeats
            )
    }

    private fun loadHabitSpinner() {

        lifecycleScope.launch {

            habitViewModel.habitList.collect { habits ->

                val names = habits.map {

                    it.name
                }

                binding.spHabit.adapter =

                    ArrayAdapter(

                        requireContext(),

                        android.R.layout.simple_spinner_dropdown_item,

                        names
                    )

                binding.spHabit.setSelection(0)

                if (habits.isNotEmpty()) {

                    selectedHabitId =

                        habits[0].habitId

                    binding.spHabit.setOnItemSelectedListener(

                        object :
                            android.widget.AdapterView.OnItemSelectedListener {

                            override fun onItemSelected(
                                parent: android.widget.AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {

                                selectedHabitId =

                                    habits[position].habitId
                            }

                            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                            }

                        }
                    )
                }

            }
        }
    }

    private fun observeReminder() {

        viewLifecycleOwner.lifecycleScope.launch {

            reminderViewModel
                .reminderList
                .collect { reminders ->

                    adapter.submitList(reminders)
                }
        }
    }

    private fun showTimePicker() {

        val calendar = Calendar.getInstance()

        TimePickerDialog(

            requireContext(),

            { _, hour, minute ->

                selectedTime =

                    String.format(

                        "%02d:%02d",

                        hour,

                        minute
                    )

                binding.tvTime.text =

                    selectedTime

            },

            calendar.get(Calendar.HOUR_OF_DAY),

            calendar.get(Calendar.MINUTE),

            true

        ).show()
    }

    private fun saveReminder() {

        if (selectedHabitId == -1L) {

            Toast.makeText(

                requireContext(),

                "No Habit",

                Toast.LENGTH_SHORT

            ).show()

            return
        }

        val reminder = Reminder(

            habitId = selectedHabitId,

            habitName = binding.spHabit.selectedItem.toString(),

            time = selectedTime,

            repeatType = binding.spRepeat.selectedItem.toString(),

            message = binding.edtMessage.text.toString(),

            enabled = binding.swEnable.isChecked
        )


        viewLifecycleOwner.lifecycleScope.launch {

            val reminderId = reminderViewModel.insertReminder(reminder)

            val calendar = java.util.Calendar.getInstance()

            val time = selectedTime.split(":")

            calendar.set(
                java.util.Calendar.HOUR_OF_DAY,
                time[0].toInt()
            )

            calendar.set(
                java.util.Calendar.MINUTE,
                time[1].toInt()
            )

            calendar.set(
                java.util.Calendar.SECOND,
                0
            )

            // Nếu giờ đã qua thì chuyển sang ngày mai
            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
            }

            ReminderAlarmManager(requireContext())
                .scheduleReminder(
                    reminderId = reminderId,
                    habitName = binding.spHabit.selectedItem.toString(),
                    message = reminder.message,
                    triggerTime = calendar.timeInMillis
                )

            Toast.makeText(
                requireContext(),
                "Reminder Saved",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}