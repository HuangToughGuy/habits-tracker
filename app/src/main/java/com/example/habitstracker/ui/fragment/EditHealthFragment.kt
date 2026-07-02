package com.example.habitstracker.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.habitstracker.R
import com.example.habitstracker.data.database.HabitsTrackerDatabase
import com.example.habitstracker.data.entity.HealthRecord
import com.example.habitstracker.data.repository.HealthRecordRepository
import com.example.habitstracker.databinding.FragmentEditHealthBinding
import com.example.habitstracker.viewmodel.HealthRecordViewModel
import com.example.habitstracker.viewmodel.HealthRecordViewModelFactory
import kotlinx.coroutines.launch
import java.util.Calendar

class EditHealthFragment :
    Fragment(R.layout.fragment_edit_health) {

    private var _binding: FragmentEditHealthBinding? = null
    private val binding get() = _binding!!

    private var recordId = -1L

    private var currentRecord: HealthRecord? = null

    private val viewModel: HealthRecordViewModel by viewModels {

        HealthRecordViewModelFactory(

            HealthRecordRepository(

                HabitsTrackerDatabase
                    .getInstance(requireContext())
                    .healthRecordDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recordId =
            arguments?.getLong("recordId") ?: -1L
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentEditHealthBinding.bind(view)

        loadRecord()

        binding.edtDate.setOnClickListener {

            showDatePicker()
        }

        binding.btnUpdateHealth.setOnClickListener {

            updateRecord()
        }
    }

    private fun loadRecord() {

        lifecycleScope.launch {

            viewModel.getRecordById(recordId)

                .collect { record ->

                    record?.let {

                        currentRecord = it

                        binding.edtDate.setText(it.date)
                        binding.edtWeight.setText(
                            it.weight.toString()
                        )
                        binding.edtWater.setText(
                            it.waterIntake.toString()
                        )
                        binding.edtSleep.setText(
                            it.sleepHours.toString()
                        )
                        binding.edtHeartRate.setText(
                            it.heartRate.toString()
                        )
                    }
                }
        }
    }

    private fun showDatePicker() {

        val calendar = Calendar.getInstance()

        DatePickerDialog(

            requireContext(),

            { _, year, month, day ->

                binding.edtDate.setText(

                    String.format(

                        "%04d-%02d-%02d",

                        year,

                        month + 1,

                        day
                    )
                )

            },

            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)

        ).show()
    }

    private fun updateRecord() {

        currentRecord?.let { old ->

            val updated = old.copy(

                date = binding.edtDate.text.toString(),
                weight = binding.edtWeight.text.toString()
                    .toFloatOrNull() ?: 0f,

                waterIntake = binding.edtWater.text.toString()
                    .toFloatOrNull() ?: 0f,
                sleepHours = binding.edtSleep.text.toString()
                    .toFloatOrNull() ?: 0f,
                heartRate = binding.edtHeartRate.text.toString()
                    .toIntOrNull() ?: 0
            )

            viewModel.updateRecord(updated)

            Toast.makeText(

                requireContext(),

                "Updated",

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