package com.example.habitstracker.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.habitstracker.R
import com.example.habitstracker.data.database.HabitsTrackerDatabase
import com.example.habitstracker.data.entity.WorkoutLog
import com.example.habitstracker.data.repository.WorkoutRepository
import com.example.habitstracker.databinding.FragmentEditWorkoutBinding
import com.example.habitstracker.viewmodel.WorkoutViewModel
import com.example.habitstracker.viewmodel.WorkoutViewModelFactory
import kotlinx.coroutines.launch
import android.app.DatePickerDialog
import java.util.Calendar


class EditWorkoutFragment :
    Fragment(R.layout.fragment_edit_workout) {

    private var _binding: FragmentEditWorkoutBinding? = null
    private val binding get() = _binding!!

    private var logId: Long = -1
    private var currentWorkout: WorkoutLog? = null
    private var selectedTypeId: Long = -1
    private var selectedWorkoutName = ""
    private val viewModel: WorkoutViewModel by viewModels {

        WorkoutViewModelFactory(

            WorkoutRepository(

                HabitsTrackerDatabase
                    .getInstance(requireContext())
                    .workoutDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        logId = arguments?.getLong("logId") ?: -1
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentEditWorkoutBinding.bind(view)

        loadWorkoutTypes()

        loadWorkout()

        binding.btnUpdateWorkout.setOnClickListener {

            updateWorkout()
        }

        binding.edtDate.setOnClickListener {

            showDatePicker()
        }
    }

    private fun showDatePicker() {

        val calendar = Calendar.getInstance()

        DatePickerDialog(

            requireContext(),

            { _, year, month, day ->

                val date = String.format(

                    "%04d-%02d-%02d",

                    year,

                    month + 1,

                    day
                )

                binding.edtDate.setText(date)

            },

            calendar.get(Calendar.YEAR),

            calendar.get(Calendar.MONTH),

            calendar.get(Calendar.DAY_OF_MONTH)

        ).show()
    }

    private fun loadWorkoutTypes() {

        lifecycleScope.launch {

            viewModel.workoutTypes.collect { list ->

                val names = list.map { it.name }

                binding.spWorkoutType.adapter =
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        names
                    )

                binding.spWorkoutType.onItemSelectedListener =
                    object :
                        android.widget.AdapterView.OnItemSelectedListener {

                        override fun onItemSelected(
                            parent: android.widget.AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {

                            selectedTypeId =
                                list[position].typeId

                            selectedWorkoutName =
                                list[position].name
                        }

                        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                        }
                    }
            }
        }
    }

    private fun loadWorkout() {

        lifecycleScope.launch {

            viewModel
                .getWorkoutLogById(logId)
                .collect { workout ->

                    workout?.let {

                        currentWorkout = it

                        binding.edtDate.setText(it.date)

                        binding.edtDuration.setText(
                            it.duration.toString()
                        )

                        binding.edtReps.setText(
                            it.reps.toString()
                        )

                        binding.edtDistance.setText(
                            it.distance.toString()
                        )

                        binding.edtCalories.setText(
                            it.calories.toString()
                        )

                        binding.edtNote.setText(
                            it.note ?: ""
                        )
                    }
                }
        }
    }

    private fun updateWorkout() {

        val duration =
            binding.edtDuration.text.toString()
                .toIntOrNull() ?: 0

        val reps =
            binding.edtReps.text.toString()
                .toIntOrNull() ?: 0

        val distance =
            binding.edtDistance.text.toString()
                .toFloatOrNull() ?: 0f

        val calories =
            binding.edtCalories.text.toString()
                .toIntOrNull() ?: 0

        val note =
            binding.edtNote.text.toString()

        currentWorkout?.let { old ->

            val updated = old.copy(

                typeId = selectedTypeId,

                workoutName = selectedWorkoutName,

                date = binding.edtDate.text.toString(),

                duration = duration,

                reps = reps,

                distance = distance,

                calories = calories,

                note = note
            )

            viewModel.updateWorkoutLog(updated)

            Toast.makeText(

                requireContext(),

                "Workout Updated",

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