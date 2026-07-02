package com.example.habitstracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.data.entity.HealthRecord
import com.example.habitstracker.databinding.ItemHealthBinding

class HealthAdapter(

    private val onItemClick: (HealthRecord) -> Unit,
    private val onDeleteClick: (HealthRecord) -> Unit

) : RecyclerView.Adapter<HealthAdapter.HealthViewHolder>() {

    private var records = listOf<HealthRecord>()

    fun submitList(newList: List<HealthRecord>) {

        records = newList

        notifyDataSetChanged()
    }

    inner class HealthViewHolder(

        private val binding: ItemHealthBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(record: HealthRecord) {

            binding.tvDate.text = record.date

            binding.tvWeight.text = "Weight: ${record.weight} kg"

            binding.tvWater.text = "Water: ${record.waterIntake} L"

            binding.tvSleep.text = "Sleep: ${record.sleepHours} h"

            binding.tvHeartRate.text = "Heart Rate: ${record.heartRate} bpm"

            binding.root.setOnClickListener {

                onItemClick(record)
            }

            binding.root.setOnLongClickListener {
                onDeleteClick(record)
                true
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HealthViewHolder {

        val binding = ItemHealthBinding.inflate(

            LayoutInflater.from(parent.context),

            parent,

            false
        )

        return HealthViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HealthViewHolder,
        position: Int
    ) {

        holder.bind(records[position])
    }

    override fun getItemCount() =
        records.size
}