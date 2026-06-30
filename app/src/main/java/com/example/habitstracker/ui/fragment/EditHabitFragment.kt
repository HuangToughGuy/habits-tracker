package com.example.habitstracker.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.habitstracker.R
import com.example.habitstracker.data.database.HabitsTrackerDatabase
import com.example.habitstracker.data.entity.Habit
import com.example.habitstracker.data.repository.HabitRepository
import com.example.habitstracker.databinding.FragmentEditHabitBinding
import com.example.habitstracker.viewmodel.HabitViewModel
import com.example.habitstracker.viewmodel.HabitViewModelFactory
import kotlinx.coroutines.launch

class EditHabitFragment : Fragment(R.layout.fragment_edit_habit) {

    private var _binding: FragmentEditHabitBinding? = null
    private val binding get() = _binding!!

    private var habitId: Long = -1

    private var currentHabit: Habit? = null

    private val viewModel: HabitViewModel by viewModels {
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

        habitId =
            arguments?.getLong("habitId")
                ?: -1

        if (habitId == -1L) {

            Toast.makeText(
                requireContext(),
                "Habit không tồn tại",
                Toast.LENGTH_SHORT
            ).show()

            parentFragmentManager.popBackStack()

            return
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentEditHabitBinding.bind(view)

        loadHabit()

        binding.btnUpdate.setOnClickListener {
            updateHabit()
        }
    }

    private fun loadHabit() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.getHabitById(habitId)
                .collect { habit ->

                    habit?.let {

                        currentHabit = it

                        binding.edtName.setText(it.name)

                        binding.edtDescription.setText(it.description)

                        binding.edtCategory.setText(it.category)

                        binding.edtTarget.setText(it.target.toString())
                    }
                }
        }
    }

    private fun updateHabit() {

        val name = binding.edtName.text.toString().trim()

        val description = binding.edtDescription.text.toString().trim()

        val category = binding.edtCategory.text.toString().trim()

        val target = binding.edtTarget.text.toString().toIntOrNull() ?: 0

        if (name.isBlank()) {

            Toast.makeText(
                requireContext(),
                "Tên habit không được để trống",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        currentHabit?.let { oldHabit ->

            val updatedHabit = oldHabit.copy(

                name = name,

                description = description,

                category = category,

                target = target
            )

            viewModel.updateHabit(updatedHabit)

            Toast.makeText(
                requireContext(),
                "Cập nhật thành công",
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
