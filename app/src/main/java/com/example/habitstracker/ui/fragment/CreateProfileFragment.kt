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
import com.example.habitstracker.data.entity.User
import com.example.habitstracker.data.repository.UserRepository
import com.example.habitstracker.databinding.FragmentCreateProfileBinding
import com.example.habitstracker.viewmodel.UserViewModel
import com.example.habitstracker.viewmodel.UserViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateProfileFragment :
    Fragment(R.layout.fragment_create_profile) {

    private var _binding: FragmentCreateProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels {

        UserViewModelFactory(

            UserRepository(

                HabitsTrackerDatabase
                    .getInstance(requireContext())
                    .userDao()
            )
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCreateProfileBinding.bind(view)

        setupGender()

        binding.edtBirthDate.setOnClickListener {

            showDatePicker()
        }

        binding.btnCreate.setOnClickListener {

            createUser()
        }
    }

    private fun setupGender() {

        binding.spGender.adapter =

            ArrayAdapter(

                requireContext(),

                android.R.layout.simple_spinner_dropdown_item,

                listOf(
                    "Male",
                    "Female",
                    "Other"
                )
            )
    }

    private fun showDatePicker() {

        val calendar = Calendar.getInstance()

        DatePickerDialog(

            requireContext(),

            { _, year, month, day ->

                binding.edtBirthDate.setText(

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

    private fun createUser() {

        val name =
            binding.edtName.text.toString().trim()

        if (name.isBlank()) {

            Toast.makeText(

                requireContext(),

                "Enter your name",

                Toast.LENGTH_SHORT

            ).show()

            return
        }

        val user = User(

            name = name,
            gender = binding.spGender.selectedItem.toString(),
            birthDate = binding.edtBirthDate.text.toString(),
            height = binding.edtHeight.text.toString()
                .toFloatOrNull() ?: 0f,

            avatar = null,

            createdAt =

            SimpleDateFormat(

                "yyyy-MM-dd HH:mm:ss",

                Locale.getDefault()

            ).format(System.currentTimeMillis())
        )

        lifecycleScope.launch {

            viewModel.insertUser(user)

            Toast.makeText(

                requireContext(),

                "Profile Created",

                Toast.LENGTH_SHORT

            ).show()

            parentFragmentManager
                .beginTransaction()
                .replace(

                    R.id.fragmentContainer,

                    HabitFragment()

                )
                .commit()
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}