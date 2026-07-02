package com.example.habitstracker.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.habitstracker.R
import com.example.habitstracker.data.database.HabitsTrackerDatabase
import com.example.habitstracker.data.repository.UserRepository
import com.example.habitstracker.databinding.FragmentProfileBinding
import com.example.habitstracker.viewmodel.UserViewModel
import com.example.habitstracker.viewmodel.UserViewModelFactory
import kotlinx.coroutines.launch

class ProfileFragment :
    Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
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

        _binding = FragmentProfileBinding.bind(view)

        observeUser()

        binding.btnEditProfile.setOnClickListener {

            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    EditProfileFragment()
                )
                .addToBackStack(null)
                .commit()

        }

    }

    private fun observeUser() {

        lifecycleScope.launch {

            userViewModel.currentUser.collect { user ->

                if (user != null) {

                    binding.tvName.text = user.name
                    binding.tvGender.text = "Gender: ${user.gender}"
                    binding.tvBirth.text = "Birth Date: ${user.birthDate}"
                    binding.tvHeight.text = "Height: ${user.height} cm"
                    binding.tvCreated.text = "Created: ${user.createdAt}"
                }

            }

        }

    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null

    }

}