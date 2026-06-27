package com.example.habitstracker.ui.adapter

import com.example.habitstracker.ui.model.HabitItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.data.entity.Habit
import com.example.habitstracker.databinding.ItemHabitBinding

class HabitAdapter(
    private val onItemClick: (Habit) -> Unit,
    private val onDeleteClick: (Habit) -> Unit,
    private val onCheckedChanged: (Habit, Boolean) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    private var habits = listOf<HabitItem>()

    fun submitList(newList: List<HabitItem>) {
        habits = newList
        notifyDataSetChanged()
    }

    inner class HabitViewHolder(
        private val binding: ItemHabitBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HabitItem) {
            val habit = item.habit

            binding.tvName.text = habit.name
            binding.tvCategory.text = habit.category
            binding.tvTarget.text =
                "Target: ${habit.target}"

            binding.tvStreak.text =
                "🔥 ${item.streak} day(s)"

            binding.cbCompleted.setOnCheckedChangeListener(null)

            binding.cbCompleted.isChecked =
                item.completedToday

            binding.cbCompleted.setOnCheckedChangeListener { _, isChecked ->

                onCheckedChanged(habit, isChecked)
            }

            binding.root.setOnClickListener {
                onItemClick(habit)
            }
            binding.root.setOnLongClickListener {
                onDeleteClick(habit)
                true
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HabitViewHolder {

        val binding = ItemHabitBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HabitViewHolder,
        position: Int
    ) {
        holder.bind(habits[position])
    }

    override fun getItemCount() = habits.size
}