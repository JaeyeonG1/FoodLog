package com.boostcampai.foodlog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boostcampai.foodlog.databinding.ItemRecyclerHomeDietBinding
import com.boostcampai.foodlog.model.Food
import com.boostcampai.foodlog.model.TodayDietModel

class HomeDietRecyclerAdapter (val calcNutrition: (List<Food>) -> String):
    ListAdapter<TodayDietModel, RecyclerView.ViewHolder>(HomeDietDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeDietViewHolder(
            ItemRecyclerHomeDietBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as HomeDietViewHolder).bind(item)
    }

    inner class HomeDietViewHolder(private val binding: ItemRecyclerHomeDietBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TodayDietModel) {
            binding.item = item
            binding.tvValue.text = calcNutrition(item.foods)
        }
    }
}

private class HomeDietDiffUtil : DiffUtil.ItemCallback<TodayDietModel>() {
    override fun areItemsTheSame(oldItem: TodayDietModel, newItem: TodayDietModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TodayDietModel, newItem: TodayDietModel): Boolean {
        return oldItem == newItem
    }
}
