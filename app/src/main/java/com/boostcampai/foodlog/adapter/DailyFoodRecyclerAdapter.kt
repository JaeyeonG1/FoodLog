package com.boostcampai.foodlog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boostcampai.foodlog.databinding.ItemRecyclerDailyFoodBinding
import com.boostcampai.foodlog.model.DailyFoodModel

class DailyFoodRecyclerAdapter :
    ListAdapter<DailyFoodModel, RecyclerView.ViewHolder>(DailyFoodDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DailyFoodViewHolder(
            ItemRecyclerDailyFoodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as DailyFoodViewHolder).bind(item)
    }

    inner class DailyFoodViewHolder(private val binding: ItemRecyclerDailyFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyFoodModel) {
            binding.item = item
        }
    }
}

private class DailyFoodDiffUtil : DiffUtil.ItemCallback<DailyFoodModel>() {
    override fun areItemsTheSame(oldItem: DailyFoodModel, newItem: DailyFoodModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DailyFoodModel, newItem: DailyFoodModel): Boolean {
        return oldItem == newItem
    }
}