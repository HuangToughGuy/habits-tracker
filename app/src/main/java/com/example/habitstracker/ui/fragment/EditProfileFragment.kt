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
import com.example.habitstracker.data.repository.UserRepository
import com.example.habitstracker.databinding.FragmentEditProfileBinding
import com.example.habitstracker.viewmodel.UserViewModel
import com.example.habitstracker.viewmodel.UserViewModelFactory
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlinx.coroutines.flow.first

class EditProfileFragment :
    Fragment(R.layout.fragment_edit_profile) {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels {

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

        _binding = FragmentEditProfileBinding.bind(view)

        setupGender()

        loadUser()

        binding.edtBirthDate.setOnClickListener {

            showDatePicker()
        }

        binding.btnUpdate.setOnClickListener {

            updateProfile()
        }
    }

    private fun setupGender() {

        val genders = listOf(

            "Male",

            "Female",

            "Other"
        )

        binding.spGender.adapter =

            ArrayAdapter(

                requireContext(),

                android.R.layout.simple_spinner_dropdown_item,

                genders
            )
    }

    private fun loadUser() {

        lifecycleScope.launch {

            userViewModel.currentUser.collect { user ->

                if (user == null) return@collect

                binding.edtName.setText(user.name)

                binding.edtBirthDate.setText(user.birthDate)

                binding.edtHeight.setText(
                    user.height.toString()
                )

                val position = when (user.gender) {

                    "Male" -> 0

                    "Female" -> 1

                    else -> 2
                }

                binding.spGender.setSelection(position)

            }

        }

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

    private fun updateProfile() {

        lifecycleScope.launch {

            val user = userViewModel.currentUser.first()

            if (user == null) return@launch

            val updated = user.copy(

                name = binding.edtName.text.toString(),

                gender =

                binding.spGender.selectedItem.toString(),

                birthDate =

                binding.edtBirthDate.text.toString(),

                height =

                binding.edtHeight.text.toString()
                    .toFloatOrNull() ?: 0f

            )

            userViewModel.updateUser(updated)

            Toast.makeText(

                requireContext(),

                "Profile Updated",

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