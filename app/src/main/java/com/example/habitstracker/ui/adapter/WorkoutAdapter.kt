package com.example.habitstracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.data.entity.WorkoutLog
import com.example.habitstracker.databinding.ItemWorkoutBinding

class WorkoutAdapter(

    private val onItemClick: (WorkoutLog) -> Unit,
    private val onDeleteClick: (WorkoutLog) -> Unit

) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    private var workouts = listOf<WorkoutLog>()

    fun submitList(list: List<WorkoutLog>) {

        workouts = list

        notifyDataSetChanged()
    }

    inner class WorkoutViewHolder(

        private val binding: ItemWorkoutBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: WorkoutLog) {

            binding.tvWorkoutName.text = workout.workoutName

            binding.tvDate.text = "Date: ${workout.date}"

            binding.tvDuration.text = "Duration: ${workout.duration} min"

            binding.tvCalories.text = "Calories: ${workout.calories}"

            binding.tvReps.text = "Reps: ${workout.reps}"

            binding.tvDistance.text = "Distance: ${workout.distance} km"

            binding.root.setOnClickListener {

                onItemClick(workout)
            }

            binding.root.setOnLongClickListener {

                onDeleteClick(workout)

                true
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutViewHolder {

        val binding = ItemWorkoutBinding.inflate(

            LayoutInflater.from(parent.context),

            parent,

            false
        )

        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: WorkoutViewHolder,
        position: Int
    ) {

        holder.bind(workouts[position])
    }

    override fun getItemCount() =
        workouts.size
}