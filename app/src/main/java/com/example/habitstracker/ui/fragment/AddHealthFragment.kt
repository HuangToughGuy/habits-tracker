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
import com.example.habitstracker.databinding.FragmentAddHealthBinding
import com.example.habitstracker.viewmodel.HealthRecordViewModel
import com.example.habitstracker.viewmodel.HealthRecordViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import android.text.Editable
import android.text.TextWatcher
import com.example.habitstracker.utils.BMIUtils

class AddHealthFragment :
    Fragment(R.layout.fragment_add_health) {

    private var _binding: FragmentAddHealthBinding? = null
    private val binding get() = _binding!!

    private var userHeight = 0f

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

        _binding = FragmentAddHealthBinding.bind(view)

        setToday()
        loadUserHeight()
        observeWeight()

        binding.edtDate.setOnClickListener {
            showDatePicker()
        }
        binding.btnSaveHealth.setOnClickListener {
            saveHealth()
        }
    }

    private fun setToday() {

        val calendar = Calendar.getInstance()

        binding.edtDate.setText(
            String.format(
                "%04d-%02d-%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        )
    }

    private fun loadUserHeight() {
        lifecycleScope.launch {
            val user = HabitsTrackerDatabase
                .getInstance(requireContext())
                .userDao()
                .getCurrentUser()
                .first()
            if (user != null) {
                userHeight = user.height
            }
        }
    }

    private fun observeWeight() {
        binding.edtWeight.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    updateBMI()
                }
                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )
    }
    private fun updateBMI() {
        if (userHeight == 0f)
            return
        val weight =
            binding.edtWeight.text
                .toString()
                .toFloatOrNull()
                ?: return

        val bmi =
            BMIUtils.calculateBMI(
                weight,
                userHeight
            )

        binding.tvBMI.text = "BMI : %.2f".format(bmi)
        binding.tvBMIStatus.text =
            "Status : ${BMIUtils.getBMIStatus(bmi)}"

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

    private fun saveHealth() {

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

            val record = HealthRecord(

                userId = user.userId,

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

            viewModel.insertRecord(record)

            Toast.makeText(
                requireContext(),
                "Saved",
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