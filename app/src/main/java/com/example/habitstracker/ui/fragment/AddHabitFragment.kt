package com.example.habitstracker.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.habitstracker.R
import com.example.habitstracker.data.database.HabitsTrackerDatabase
import com.example.habitstracker.data.entity.Habit
import com.example.habitstracker.data.repository.HabitRepository
import com.example.habitstracker.databinding.FragmentAddHabitBinding
import com.example.habitstracker.viewmodel.HabitViewModel
import com.example.habitstracker.viewmodel.HabitViewModelFactory
import kotlinx.coroutines.flow.first
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AddHabitFragment : Fragment(R.layout.fragment_add_habit) {

    private var _binding: FragmentAddHabitBinding? = null
    private val binding get() = _binding!!

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

        _binding = FragmentAddHabitBinding.bind(view)

        binding.btnSave.setOnClickListener {

            val name = binding.edtName.text.toString()
            val description = binding.edtDescription.text.toString()
            val category = binding.edtCategory.text.toString()
            val target = binding.edtTarget.text.toString().toIntOrNull() ?: 0

            if (name.isBlank()) {
                Toast.makeText(requireContext(), "Nhập tên habit", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {

                val db = HabitsTrackerDatabase.getInstance(requireContext())

                val user = db.userDao()
                    .getCurrentUser()
                    .first()   //  LẤY VALUE THẬT

                if (user == null) {
                    Toast.makeText(requireContext(), "Chưa có user", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val habit = Habit(
                    userId = user.userId,   //  đúng FK
                    name = name,
                    description = description,
                    category = category,
                    target = target,
                    icon = "",
                    color = "",
                    createdDate = System.currentTimeMillis().toString(),
                    isActive = true
                )

                viewModel.insertHabit(habit)

                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}