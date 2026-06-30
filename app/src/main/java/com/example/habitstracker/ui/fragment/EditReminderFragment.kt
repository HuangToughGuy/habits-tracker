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
import com.example.habitstracker.alarm.ReminderAlarmManager
import com.example.habitstracker.data.database.HabitsTrackerDatabase
import com.example.habitstracker.data.entity.Reminder
import com.example.habitstracker.data.repository.HabitRepository
import com.example.habitstracker.data.repository.ReminderRepository
import com.example.habitstracker.databinding.FragmentEditReminderBinding
import com.example.habitstracker.viewmodel.HabitViewModel
import com.example.habitstracker.viewmodel.HabitViewModelFactory
import com.example.habitstracker.viewmodel.ReminderViewModel
import com.example.habitstracker.viewmodel.ReminderViewModelFactory
import kotlinx.coroutines.launch
import java.util.Calendar

class EditReminderFragment : Fragment(R.layout.fragment_edit_reminder) {

    private var _binding: FragmentEditReminderBinding? = null
    private val binding get() = _binding!!

    private var reminderId: Long = -1

    private var currentReminder: Reminder? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reminderId =
            arguments?.getLong("reminderId") ?: -1
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentEditReminderBinding.bind(view)

        setupRepeatSpinner()

        loadHabitSpinner()

        loadReminder()

        binding.btnPickTime.setOnClickListener {

            showTimePicker()
        }

        binding.btnUpdateReminder.setOnClickListener {

            updateReminder()
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

                binding.spHabit.adapter =
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        habits.map { it.name }
                    )

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

                        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
                    }
                )

                currentReminder?.let { reminder ->

                    val index = habits.indexOfFirst {

                        it.habitId == reminder.habitId
                    }

                    if (index != -1) {

                        binding.spHabit.setSelection(index)
                    }
                }
            }
        }
    }

    private fun loadReminder() {

        lifecycleScope.launch {

            reminderViewModel
                .getReminderById(reminderId)
                .collect { reminder ->

                    reminder ?: return@collect

                    currentReminder = reminder

                    selectedTime = reminder.time

                    binding.tvTime.text = reminder.time

                    binding.edtMessage.setText(
                        reminder.message
                    )

                    binding.swEnable.isChecked =
                        reminder.enabled

                    val repeatIndex =
                        when (reminder.repeatType) {

                            "Daily" -> 0

                            "Weekly" -> 1

                            else -> 2
                        }

                    binding.spRepeat.setSelection(
                        repeatIndex
                    )
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

    private fun updateReminder() {

        currentReminder?.let { old ->

            val updated = old.copy(

                habitId = selectedHabitId,

                habitName = binding.spHabit.selectedItem.toString(),

                time = selectedTime,

                repeatType = binding.spRepeat.selectedItem.toString(),

                message = binding.edtMessage.text.toString(),

                enabled = binding.swEnable.isChecked
            )

            reminderViewModel.updateReminder(updated)

            val calendar = Calendar.getInstance()

            val time = selectedTime.split(":")

            calendar.set(
                Calendar.HOUR_OF_DAY,
                time[0].toInt()
            )

            calendar.set(
                Calendar.MINUTE,
                time[1].toInt()
            )

            calendar.set(
                Calendar.SECOND,
                0
            )

            if (calendar.timeInMillis <= System.currentTimeMillis()) {

                calendar.add(
                    Calendar.DAY_OF_YEAR,
                    1
                )
            }

            val alarmManager =
                ReminderAlarmManager(requireContext())

            alarmManager.cancelReminder(
                updated.reminderId
            )

            if (updated.enabled) {

                alarmManager.scheduleReminder(

                    updated.reminderId,

                    updated.habitName,

                    updated.message,

                    updated.repeatType,

                    updated.enabled,

                    calendar.timeInMillis
                )
            }

            Toast.makeText(

                requireContext(),

                "Reminder Updated",

                Toast.LENGTH_SHORT

            ).show()

            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}