package com.example.habitstracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.data.entity.Reminder
import com.example.habitstracker.databinding.ItemReminderBinding

class ReminderAdapter(

    private val onItemClick: (Reminder) -> Unit,

    private val onDeleteClick: (Reminder) -> Unit,

    private val onEnableChanged: (Reminder, Boolean) -> Unit

) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    private var reminders = listOf<Reminder>()

    fun submitList(list: List<Reminder>) {

        reminders = list

        notifyDataSetChanged()
    }

    inner class ReminderViewHolder(

        private val binding: ItemReminderBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(reminder: Reminder) {

            binding.tvHabitName.text = reminder.habitName

            binding.tvTime.text = reminder.time

            binding.tvRepeat.text = "Repeat: ${reminder.repeatType}"

            binding.tvMessage.text = reminder.message

            binding.swEnabled.setOnCheckedChangeListener(null)

            binding.swEnabled.isChecked = reminder.enabled

            binding.swEnabled.setOnCheckedChangeListener { _, checked ->

                onEnableChanged(reminder, checked)
            }

            binding.root.setOnClickListener {

                onItemClick(reminder)
            }

            binding.root.setOnLongClickListener {

                onDeleteClick(reminder)

                true
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReminderViewHolder {

        val binding = ItemReminderBinding.inflate(

            LayoutInflater.from(parent.context),

            parent,

            false
        )

        return ReminderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ReminderViewHolder,
        position: Int
    ) {

        holder.bind(reminders[position])
    }

    override fun getItemCount() = reminders.size
}