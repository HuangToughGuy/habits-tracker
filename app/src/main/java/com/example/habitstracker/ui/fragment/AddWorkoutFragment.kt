package com.example.habitstracker.ui.fragment

import android.app.DatePickerDialog
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
import com.example.habitstracker.databinding.FragmentAddWorkoutBinding
import com.example.habitstracker.viewmodel.WorkoutViewModel
import com.example.habitstracker.viewmodel.WorkoutViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

class AddWorkoutFragment : Fragment(R.layout.fragment_add_workout) {

    private var _binding: FragmentAddWorkoutBinding? = null
    private val binding get() = _binding!!

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

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddWorkoutBinding.bind(view)
        binding.edtDate.setOnClickListener {
            showDatePicker()
        }
        val calendar = Calendar.getInstance()
        binding.edtDate.setText(

            String.format(

                "%04d-%02d-%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        )

        loadWorkoutTypes()
        binding.btnSaveWorkout.setOnClickListener {
            saveWorkout()
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

                val names = list.map {

                    it.name
                }

                binding.spWorkoutType.adapter =

                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        names
                    )

                if (list.isNotEmpty()) {
                    selectedTypeId = list[0].typeId
                    selectedWorkoutName = list[0].name
                    binding.spWorkoutType.onItemSelectedListener =

                        object : android.widget.AdapterView.OnItemSelectedListener {

                            override fun onItemSelected(
                                parent: android.widget.AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {

                                selectedTypeId = list[position].typeId
                                selectedWorkoutName = list[position].name
                            }

                            override fun onNothingSelected(
                                parent: android.widget.AdapterView<*>?
                            ) {
                            }
                        }
                }
            }
        }
    }

    private fun saveWorkout() {

        val duration =
            binding.edtDuration.text
                .toString()
                .toIntOrNull() ?: 0

        val reps =
            binding.edtReps.text
                .toString()
                .toIntOrNull() ?: 0

        val distance =
            binding.edtDistance.text
                .toString()
                .toFloatOrNull() ?: 0f

        val calories =
            binding.edtCalories.text
                .toString()
                .toIntOrNull() ?: 0

        val note =
            binding.edtNote.text
                .toString()

        if (selectedTypeId == -1L) {

            Toast.makeText(

                requireContext(),

                "Please select workout",

                Toast.LENGTH_SHORT

            ).show()

            return
        }

        lifecycleScope.launch {

            val db = HabitsTrackerDatabase.getInstance(requireContext())

            val user = db.userDao()
                .getCurrentUser()
                .first()

            if (user == null) {

                Toast.makeText(
                    requireContext(),
                    "Chưa có user",
                    Toast.LENGTH_SHORT
                ).show()

                return@launch
            }

            val workout = WorkoutLog(

                userId = user.userId,

                typeId = selectedTypeId,

                workoutName = selectedWorkoutName,

                date = binding.edtDate.text.toString(),

                duration = duration,

                reps = reps,

                distance = distance,

                calories = calories,

                note = note
            )

            viewModel.insertWorkoutLog(workout)

            Toast.makeText(

                requireContext(),

                "Workout Saved",

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